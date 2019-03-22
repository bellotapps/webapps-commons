/*
 * Copyright 2019 BellotApps
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

import org.glassfish.jersey.server.internal.inject.ConfiguredValidator;
import org.glassfish.jersey.server.model.Invocable;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.executable.ValidateOnExecution;
import javax.validation.metadata.BeanDescriptor;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A Concrete implementation of a {@link ConfiguredValidator}.
 */
public class JerseyValidator<E extends ConstraintViolationException> implements ConfiguredValidator {

    /**
     * A {@link Validator} to which the validation process is delegated to.
     */
    private final Validator delegate;

    /**
     * A flag indicating whether execution validation must be performed.
     */
    private final boolean executionValidationEnabled;

    /**
     * A {@link ConstraintViolationExceptionCreator} that create {@link ConstraintViolationException}s
     * from a {@link Set} of {@link ConstraintViolation}s.
     */
    private final ConstraintViolationExceptionCreator constraintViolationExceptionSupplier;


    /**
     * Constructor.
     *
     * @param delegate                             A {@link Validator} to which the validation process is delegated to.
     * @param executionValidationEnabled           A flag indicating whether execution validation must be performed.
     * @param constraintViolationExceptionSupplier A {@link ConstraintViolationExceptionCreator}
     *                                             that create {@link ConstraintViolationException}s
     *                                             from a {@link Set} of {@link ConstraintViolation}s.
     */
    public JerseyValidator(
            final Validator delegate,
            final boolean executionValidationEnabled,
            final ConstraintViolationExceptionCreator constraintViolationExceptionSupplier) {
        this.delegate = delegate;
        this.executionValidationEnabled = executionValidationEnabled;
        this.constraintViolationExceptionSupplier = constraintViolationExceptionSupplier;
    }

    @Override
    public void validateResourceAndInputParams(
            final Object resource,
            final Invocable resourceMethod,
            final Object[] args) throws ConstraintViolationException {

        final var constraintViolations = new HashSet<ConstraintViolation<Object>>();
        final var beanDescriptor = getConstraintsForClass(resource.getClass());

        // First validate resource as a bean (i.e properties and fields)
        if (beanDescriptor.isBeanConstrained()) {
            final var violations = validate(resource);
            constraintViolations.addAll(violations);
        }

        // Then, validate execution if execution validation is enabled and the Invocable is set
        if (executionValidationEnabled && resourceMethod != null) {
            final var handlingMethod = resourceMethod.getHandlingMethod();

            // Get the method descriptor
            final var methodDescriptor = beanDescriptor.getConstraintsForMethod(
                    handlingMethod.getName(),
                    handlingMethod.getParameterTypes()
            );

            // Continue only if the methodDescriptor is not null and has constrained parameters
            if (methodDescriptor != null && methodDescriptor.hasConstrainedParameters()) {
                final var violations = validateMethod(
                        resource,
                        resourceMethod.getDefinitionMethod(),
                        handlingMethod,
                        (r, h) -> forExecutables().validateParameters(r, h, args)
                );
                constraintViolations.addAll(violations);
            }
        }

        // If there were constraint violations, then throw a ConstraintViolationException
        if (!constraintViolations.isEmpty()) {
            throw constraintViolationExceptionSupplier.create(constraintViolations);
        }
    }


    @Override
    public void validateResult(
            final Object resource,
            final Invocable resourceMethod,
            final Object result) throws ConstraintViolationException {

        // validate execution if execution validation is enabled
        if (executionValidationEnabled) {
            final var constraintViolations = new HashSet<ConstraintViolation<Object>>();
            final var handlingMethod = resourceMethod.getHandlingMethod();

            final var beanDescriptor = getConstraintsForClass(resource.getClass());
            final var methodDescriptor = beanDescriptor.getConstraintsForMethod(
                    handlingMethod.getName(),
                    handlingMethod.getParameterTypes()
            );

            // Continue only if the methodDescriptor is not null and has constrained return value
            if (methodDescriptor != null && methodDescriptor.hasConstrainedReturnValue()) {
                final var resourceViolations = validateMethod(
                        resource,
                        resourceMethod.getDefinitionMethod(),
                        handlingMethod,
                        (r, h) -> forExecutables().validateReturnValue(r, h, result)
                );
                constraintViolations.addAll(resourceViolations);

                // In case the result is a Response, validate the contained entity also
                if (result instanceof Response) {
                    final var entityViolations = validateMethod(
                            resource,
                            resourceMethod.getDefinitionMethod(),
                            handlingMethod,
                            (r, h) -> forExecutables().validateReturnValue(r, h, ((Response) result).getEntity())
                    );
                    constraintViolations.addAll(entityViolations);
                }
            }

            // If there were constraint violations, then throw a ConstraintViolationException
            if (!constraintViolations.isEmpty()) {
                throw constraintViolationExceptionSupplier.create(constraintViolations);
            }
        }
    }


    @Override
    public <T> Set<ConstraintViolation<T>> validate(final T object, final Class<?>... groups) {
        return delegate.validate(object, groups);
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateProperty(
            final T object,
            final String propertyName,
            final Class<?>... groups) {
        return delegate.validateProperty(object, propertyName, groups);
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateValue(
            final Class<T> beanType,
            final String propertyName,
            final Object value,
            final Class<?>... groups) {
        return delegate.validateValue(beanType, propertyName, value, groups);
    }

    @Override
    public BeanDescriptor getConstraintsForClass(final Class<?> clazz) {
        return delegate.getConstraintsForClass(clazz);
    }

    @Override
    public <T> T unwrap(final Class<T> type) {
        return delegate.unwrap(type);
    }

    @Override
    public ExecutableValidator forExecutables() {
        return delegate.forExecutables();
    }


    // ================================================================================================================
    // Helpers
    // ================================================================================================================

    /**
     * A {@link FunctionalInterface} that defines a method that takes a object of type {@code T} and a {@link Method}
     * and performs the validation of the {@link Method}, which is invoked in the given object.
     *
     * @param <T> The concrete type of object.
     */
    @FunctionalInterface
    private interface MethodValidationProcess<T> {

        /**
         * Performs the validation process.
         *
         * @param resource       The {@link Object} on which the method to validate is invoked.
         * @param handlingMethod The {@link Method} for which the return value constraints is validated.
         * @return A {@link Set} with the {@link ConstraintViolation} caused by this validation.
         * @throws IllegalArgumentException If {@code null} is passed for any of the object,
         *                                  method or groups parameters or if parameters don't match with each other
         * @throws ValidationException      If a non recoverable error happens during the
         *                                  validation process
         */
        Set<ConstraintViolation<T>> validate(final T resource, final Method handlingMethod)
                throws IllegalArgumentException, ValidationException;
    }


    /**
     * Performs the validation of a {@link Method}, checking before if the validation must be performed,
     * according to {@link #mustValidateMethod(Class, Method, Method)}.
     *
     * @param resource          The {@link Object} on which the method to validate is invoked.
     * @param definitionMethod  The {@link Method} to be examined.
     * @param handlingMethod    The {@link Method} used for cache.
     * @param validationProcess A {@link MethodValidationProcess} for the {@code resource} and {@code handlingMethod}.
     * @param <T>               The concrete type of the resource.
     * @return A {@link Set} with the {@link ConstraintViolation} caused by this validation.
     * @throws IllegalArgumentException If any of the arguments is {@code null}.
     * @throws ValidationException      If a non recoverable error happens during the validation process.
     */
    private static <T> Set<ConstraintViolation<T>> validateMethod(
            final T resource,
            final Method definitionMethod,
            final Method handlingMethod,
            final MethodValidationProcess<T> validationProcess) throws IllegalArgumentException, ValidationException {

        // First, assert arguments
        Assert.notNull(resource, "The resource must not be null");
        Assert.notNull(definitionMethod, "The definition method must not be null");
        Assert.notNull(handlingMethod, "The handling method must not be null");
        Assert.notNull(validationProcess, "The validation process must not be null");

        // Then, check if method must be validated. Perform validation if yes, or return an empty set if not.
        return mustValidateMethod(resource.getClass(), definitionMethod, handlingMethod) ?
                validationProcess.validate(resource, handlingMethod) :
                Collections.emptySet();
    }

    /**
     * Determine whether the given resource {@code method} to-be-executed on the given {@code klass} should be
     * validated.
     * The difference between this and {@link #validateGetter} method is that this method returns {@code true} if the
     * {@code method} is getter and validating getter method is not explicitly disabled by the
     * {@link ValidateOnExecution} annotation in the class hierarchy.
     *
     * @param clazz            {@link Class} on which the method will be invoked.
     * @param method           {@link Method} to be examined.
     * @param validationMethod {@link Method} used for cache.
     * @return {@code true} if the method should be validated, {@code false} otherwise.
     */
    private static boolean mustValidateMethod(
            final Class<?> clazz,
            final Method method,
            final Method validationMethod) {
        return true; // TODO: implement
    }

}
