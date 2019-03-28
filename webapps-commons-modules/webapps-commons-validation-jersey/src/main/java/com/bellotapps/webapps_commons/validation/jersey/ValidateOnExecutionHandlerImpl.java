/*
 * Copyright 2018-2019 BellotApps
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bellotapps.webapps_commons.validation.jersey;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.validation.ValidationException;
import javax.validation.executable.ExecutableType;
import javax.validation.executable.ValidateOnExecution;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Defines behaviour for an object than can handle the
 * {@link javax.validation.executable.ValidateOnExecution} annotation
 * when defining whether a method should be validated.
 */
public class ValidateOnExecutionHandlerImpl implements ValidateOnExecutionHandler {

    /**
     * A {@link Map} holding already calculated "must validate" flags for methods.
     */
    private final Map<Method, Boolean> mustValidateMethodCache;

    /**
     * The default {@link ExecutableType}s.
     */
    private final Set<ExecutableType> defaultValidatedExecutableTypes;


    /**
     * Constructor.
     *
     * @param defaultValidatedExecutableTypes The default {@link ExecutableType}s.
     */
    public ValidateOnExecutionHandlerImpl(final Set<ExecutableType> defaultValidatedExecutableTypes) {
        this.defaultValidatedExecutableTypes = defaultValidatedExecutableTypes;
        this.mustValidateMethodCache = new ConcurrentHashMap<>();
    }


    @Override
    public boolean mustValidateMethod(final Class<?> clazz, final Method method, final Method validationMethod) {
        Assert.notNull(clazz, "The class must not be null");
        Assert.notNull(method, "The method must not be null");
        Assert.notNull(validationMethod, "The validation method must not be null");
        return Optional.ofNullable(mustValidateMethodCache.get(validationMethod))
                .orElse(calculateAndCache(clazz, method, validationMethod));
    }

    /**
     * Calculates the "must validate" flag for the given {@code clazz}, {@code method} and {@code validationMethod}.
     *
     * @param clazz            The {@link Class} on which the method will be invoked.
     * @param method           The {@link Method} to be examined.
     * @param validationMethod {@link Method} used for cache.
     * @return {@code true} if the method should be validated, {@code false} otherwise.
     */
    private boolean calculateAndCache(final Class<?> clazz, final Method method, final Method validationMethod) {
        final var calculated = processAnnotation(clazz, method)
                .orElse(processGlobalConfiguration(method, defaultValidatedExecutableTypes));
        mustValidateMethodCache.putIfAbsent(validationMethod, calculated);
        return calculated;
    }


    /**
     * Processes the {@link ValidateOnExecution} annotation in the given {@code method},
     * invoked in the given {@code clazz}.
     *
     * @param clazz  The {@link Class} on which the method will be invoked.
     * @param method The {@link Method} to be examined.
     * @return An {@link Optional} of {@link Boolean}, which defines whether the method must be validated,
     * or empty {@link Optional} if validation cannot be determined
     * (i.e {@link ValidateOnExecution} annotation is not present).
     */
    private static Optional<Boolean> processAnnotation(final Class<?> clazz, final Method method) {
        final var hierarchy = getTypeHierarchy(clazz);
        // Examine each superclass / implemented interface to determine validation based on the annotation.
        // Note that the hierarchy queue is ordered by generality
        // (i.e first interfaces, in order of generality, then superclasses, starting by the Object class)
        while (!hierarchy.isEmpty()) {
            final var superClass = hierarchy.removeFirst();
            // Check if method is defined in this class
            final var optional = getMethodInClass(superClass, method)
                    .map(overriddenMethod -> {
                        // If method is defined in the class, then check if the annotation is present
                        final var typesOptional = getExecutableTypes(overriddenMethod);
                        // If present, check if overriding methods are annotated (THEY MUST NOT BE ANNOTATED).
                        if (typesOptional.isPresent()) {
                            checkOverridingIsAnnotated(superClass, method, hierarchy);
                        }
                        return typesOptional
                                // If the annotation was present, the optional is not empty.
                                // Map the content in it (i.e the ExecutableType Set) into the check result
                                .map(types -> validateMethod(overriddenMethod, types, true))
                                // Or, if no annotation present on the method,
                                // try on the class
                                // (taking into account only if there is other than IMPLICIT).
                                .or(
                                        () -> getExecutableTypes(superClass)
                                                .filter(ValidateOnExecutionHandlerImpl::notOnlyImplicit)
                                                // If present on the class (with other than IMPLICIT),
                                                // map into the check result.
                                                .map(types -> validateMethod(overriddenMethod, types, false))
                                );
                    })
                    .flatMap(Function.identity()); // FlatMap into a single optional
            // If the Optional is not empty for this class, then the result is already achieved.
            if (optional.isPresent()) {
                return optional;
            }
        }
        // In case the hierarchy Deque is emptied, then validation could not be determined
        // (i.e any of the implemented interfaces / superclasses hosts a ValidateOnExecution annotation).
        return Optional.empty();
    }

    /**
     * Processes global configuration to determine if validation must be performed for the given {@code method}.
     *
     * @param method                          The {@link Method} to be examined.
     * @param defaultValidatedExecutableTypes The default {@link ExecutableType}s.
     * @return {@code} true if validation must be performed (according to global configuration),
     * or {@code false} otherwise.
     */
    private static boolean processGlobalConfiguration(
            final Method method,
            final Set<ExecutableType> defaultValidatedExecutableTypes) {
        return validateMethod(method, defaultValidatedExecutableTypes, false);
    }


    /**
     * Get a class hierarchy for the given {@code clazz} suitable to be looked for {@link ValidateOnExecution} annotation
     * in order according to the priority defined by Bean Validation spec (superclasses, interfaces).
     *
     * @param clazz The {@link Class} for which the hierarchy will be obtained for.
     * @return The {@link Class} hierarchy.
     */
    private static Deque<Class<?>> getTypeHierarchy(final Class<?> clazz) throws IllegalArgumentException {
        final var result = new LinkedHashSet<Class<?>>();
        final var recursiveStack = (Deque<Class<?>>) new LinkedList<Class<?>>();
        final var processed = new HashSet<Class<?>>();
        final var classes = new LinkedList<Class<?>>();
        var currentClass = clazz;

        while (currentClass != null) {
            final var classInterfaces = Arrays.asList(currentClass.getInterfaces());
            recursiveStack.addAll(classInterfaces);
            while (!recursiveStack.isEmpty()) {
                var currentInterface = recursiveStack.peek(); // Get first interface in the stack
                // First check if already processed
                if (processed.contains(currentInterface)) {
                    // If processed, then just add the interface to the result list, and remove from stack
                    result.add(currentInterface);
                    recursiveStack.pop();
                } else {
                    // If not processed, then process and add to the processed set
                    final var interfaces = currentInterface.getInterfaces(); // Get superinterfaces
                    processed.add(currentInterface);
                    // If there is no more superinterfaces (i.e is root interface)
                    if (interfaces.length == 0) {
                        // Add the root interface to the result list, and remove it from the stack
                        result.add(currentInterface);
                        recursiveStack.pop();
                    } else {
                        // If there are more superinterfaces, then add them all to the stack in reversed order
                        for (int i = interfaces.length - 1; i >= 0; i--) {
                            recursiveStack.push(interfaces[i]);
                        }
                    }
                }
            }
            classes.add(currentClass);
            currentClass = currentClass.getSuperclass();
        }
        result.addAll(classes); // Append all classes at the end
        return new LinkedList<>(result);
    }

    /**
     * Finds the given {@code method} in the given {@code clazz}.
     * <p>
     * If a public method on the given {@code clazz} has the same name and parameters as the given {@code method},
     * then that public method is returned in an {@link Optional}.
     * <p>
     * Otherwise, if a public method on the given {@code clazz} has the same name and the same number of parameters
     * as the given {@code method}, and each generic parameter type of that public method are understood to be equal
     * to the parameters in the given {@code method}, then that public method is returned in an {@link Optional}.
     * <p>
     * Otherwise, an empty {@link Optional} is returned.
     *
     * @param clazz  The {@link Class} in which the public method will be searched.
     * @param method The {@link Method} to be searched.
     * @return An {@link Optional} with the {@link Method} in the {@link Class} if it is found, or empty otherwise.
     * @see ValidateOnExecutionHandlerImpl#compareParameterTypes(Type, Type)
     * @see ValidateOnExecutionHandlerImpl#compareParameterTypes(Type[], Type[])
     * @see ValidateOnExecutionHandlerImpl#checkTypeBounds(Class, Type[])
     */
    private static Optional<Method> getMethodInClass(final Class<?> clazz, final Method method) {
        final var methodName = method.getName();
        final var parameterTypes = method.getParameterTypes();
        try {
            return Optional.of(clazz.getMethod(methodName, parameterTypes));
        } catch (final NoSuchMethodException e) {
            return Arrays.stream(clazz.getMethods())
                    .filter(m -> m.getName().equals(methodName))
                    .filter(m -> Arrays.equals(m.getParameterTypes(), parameterTypes))
                    .filter(m -> compareParameterTypes(method.getGenericParameterTypes(), m.getGenericParameterTypes()))
                    .findFirst();
        }
    }

    /**
     * Compares the given {@code Type} arrays, taking into account generics.
     *
     * @param ts1 The first {@link Type} array.
     * @param ts2 The second {@link Type} array.
     * @return {@code true} if the given {@link Type} arrays are understood to be equal, {@code false} otherwise.
     */
    private static boolean compareParameterTypes(final Type[] ts1, final Type[] ts2) {
        Assert.notNull(ts1, "The first Type array must not be null");
        Assert.notNull(ts2, "The second Type array must not be null");
        Assert.isTrue(ts1.length == ts2.length, "Both type arrays must have the same length");
        return IntStream.range(0, ts1.length)
                .mapToObj(i -> new Type[]{ts1[i], ts2[i]})
                .allMatch(arr -> compareParameterTypes(arr[0], arr[1]));
    }

    /**
     * Compares the given {@code Type}s, taking into account generics.
     *
     * @param t1 The first {@link Type}.
     * @param t2 The second {@link Type}.
     * @return {@code true} if the given {@link Type}s are understood to be equal, {@code false} otherwise.
     */
    private static boolean compareParameterTypes(final Type t1, final Type t2) {
        Assert.notNull(t1, "The first Type must not be null");
        Assert.notNull(t2, "The second Type must not be null");
        // First check if they are equal.
        if (t1.equals(t2)) {
            return true;
        }
        if (t1 instanceof Class<?>) {
            final var clazz = (Class<?>) t1;
            if (t2 instanceof Class<?>) {
                return ClassUtils.isAssignable((Class<?>) t2, clazz);
            } else if (t2 instanceof TypeVariable) {
                return checkTypeBounds(clazz, ((TypeVariable) t2).getBounds());
            }
        }
        return t1 instanceof TypeVariable && t2 instanceof TypeVariable;
    }


    /**
     * Checks whether the the given {@code type} is between the given {@code bounds}.
     *
     * @param type   The type to be checked.
     * @param bounds The bound to be checked.
     * @return {@code true} if the given {@code type} is between the given {@code bounds}, or {@code false} otherwise.
     */
    private static boolean checkTypeBounds(final Class<?> type, final Type[] bounds) {
        Assert.notNull(type, "The type must not be null");
        Assert.notNull(bounds, "The bounds arrays must not be null");
        return Arrays.stream(bounds)
                .filter(b -> b instanceof Class<?>)
                .allMatch(b -> ClassUtils.isAssignable((Class<?>) b, type));
    }

    /**
     * Determine whether the given {@code method} should be validated,
     * depending on the given {@code executableTypes}.
     *
     * @param method          The {@link Method} to be examined.
     * @param executableTypes {@link ExecutableType}s assigned to the {@link Method}.
     * @param allowImplicit   A flag indicating whether {@link ExecutableType#IMPLICIT} should be taken into account.
     * @return {@code true} if the method should be validated, or {@code false} otherwise.
     */
    private static boolean validateMethod(
            final Method method,
            final Set<ExecutableType> executableTypes,
            final boolean allowImplicit) {
        if (executableTypes.contains(ExecutableType.ALL)
                || (allowImplicit && executableTypes.contains(ExecutableType.IMPLICIT))) {
            return true;
        }
        return isGetter(method) ?
                executableTypes.contains(ExecutableType.GETTER_METHODS) :
                executableTypes.contains(ExecutableType.NON_GETTER_METHODS);
    }

    /**
     * Return a {@link Set} OF {@link ExecutableType}
     * containing those contained in a {@link ValidateOnExecution} annotation belonging to the given {@code element}.
     *
     * @param element The {@link AnnotatedElement} to be examined for the {@link ValidateOnExecution}.
     * @return An {@link Optional} containing the {@link Set} of {@link ExecutableType}
     * with those values in the annotation, if the annotation is present in the {@link AnnotatedElement}.
     * Otherwise, an empty {@link Optional} is returned.
     */
    private static Optional<Set<ExecutableType>> getExecutableTypes(final AnnotatedElement element) {
        return Optional.ofNullable(element.getAnnotation(ValidateOnExecution.class))
                .map(ValidateOnExecution::type)
                .map(Set::of);
    }

    /**
     * Verifies whether any of the classes in the given {@code hierarchy} hosts a {@link ValidateOnExecution}
     * annotation in the given {@code method} (if it declares it).
     *
     * @param definingClass The most general {@link Class} that declares the method.
     * @param method        The {@link Method} to be examined.
     * @param hierarchy     The {@link Class} hierarchy.
     * @throws ValidationException If any of the {@link Class}es in the {@code hierarchy} {@link Deque} hosts a
     *                             {@link ValidateOnExecution} annotation in the given {@code method}
     *                             (if it declares it).
     */
    private static void checkOverridingIsAnnotated(final Class<?> definingClass, final Method method,
                                                   final Deque<Class<?>> hierarchy)
            throws ValidationException {
        final var subclassHosts = hierarchy.stream().anyMatch(
                clazz ->
                        getMethodInClass(clazz, method)
                                // Skip if declaring class is the given definingClass
                                // (i.e clazz is interface or abstract)
                                .filter(m -> !m.getDeclaringClass().equals(definingClass))
                                // Map overridden method into executable types on that method, or on the class
                                .map(overriddenMethod ->
                                        // Try first on the method
                                        getExecutableTypes(overriddenMethod)
                                                // Or, if no ValidateOnExecution annotation on the method,
                                                // try on the class
                                                // (taking into account only if there is other than IMPLICIT).
                                                .or(
                                                        () -> getExecutableTypes(clazz)
                                                                .filter(ValidateOnExecutionHandlerImpl::notOnlyImplicit)
                                                )

                                )
                                .flatMap(Function.identity()) // FlatMap into a single optional
                                .isPresent() // Return true if present (i.e is annotated), or false otherwise
        );
        if (subclassHosts) {
            throw new ValidationException("Multiple ValidateOnExecution for "
                    + method.getDeclaringClass().getName() + "#" + method.getName());
        }
    }

    /**
     * Indicates whether the given {@code types} {@link Set} contains more than one element,
     * or it not contains {@link ExecutableType#IMPLICIT}.
     *
     * @param types The {@link Set} to be checked.
     * @return {@code true} if there is more than one element in the {@link Set}
     * or the only element contained is not {@link ExecutableType#IMPLICIT},
     * or {@code false} otherwise.
     */
    private static boolean notOnlyImplicit(final Set<ExecutableType> types) {
        return !(types.size() == 1 && types.contains(ExecutableType.IMPLICIT));
    }

    /**
     * Determine whether the given {@code method} is a getter.
     *
     * @param method The {@link Method} to be examined.
     * @return {@code true} if the {@link Method} is a getter, {@code false} otherwise.
     */
    private static boolean isGetter(final Method method) {
        if (method.getParameterTypes().length == 0 && Modifier.isPublic(method.getModifiers())) {
            final var methodName = method.getName();

            if (methodName.startsWith("get") && methodName.length() > 3) {
                return !void.class.equals(method.getReturnType());
            } else if (methodName.startsWith("is") && methodName.length() > 2) {
                return boolean.class.equals(method.getReturnType()) || Boolean.class.equals(method.getReturnType());
            }
        }
        return false;
    }
}
