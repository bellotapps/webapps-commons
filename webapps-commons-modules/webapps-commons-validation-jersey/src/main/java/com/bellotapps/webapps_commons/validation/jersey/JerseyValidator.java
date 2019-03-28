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

import org.glassfish.jersey.server.internal.inject.ConfiguredValidator;
import org.glassfish.jersey.server.model.Invocable;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.executable.ValidateOnExecution;
import javax.validation.metadata.BeanDescriptor;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * A Concrete implementation of a {@link ConfiguredValidator}.
 */
public class JerseyValidator implements ConfiguredValidator {

    /**
     * A {@link Validator} to which the validation process is delegated to.
     */
    private final Validator delegate;

    /**
     * A flag indicating whether execution validation must be performed.
     */
    private final boolean executionValidationEnabled;

    private final ValidateOnExecutionHandler validateOnExecutionHandler;

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
     * @param validateOnExecutionHandler           A {@link ValidateOnExecutionHandler} which defines whether a method
     *                                             must be validated according to the {@link ValidateOnExecution}
     *                                             annotation.
     * @param constraintViolationExceptionSupplier A {@link ConstraintViolationExceptionCreator}
     *                                             that create {@link ConstraintViolationException}s
     *                                             from a {@link Set} of {@link ConstraintViolation}s.
     */
    public JerseyValidator(
            final Validator delegate,
            final boolean executionValidationEnabled,
            final ValidateOnExecutionHandler validateOnExecutionHandler,
            final ConstraintViolationExceptionCreator constraintViolationExceptionSupplier) {
        this.delegate = delegate;
        this.executionValidationEnabled = executionValidationEnabled;
        this.validateOnExecutionHandler = validateOnExecutionHandler;
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

            // Continue if the methodDescriptor is not null, has constrained parameters, and method must be validated
            if (methodDescriptor != null
                    && methodDescriptor.hasConstrainedParameters()
                    && mustValidateMethod(resource.getClass(), resourceMethod.getDefinitionMethod(), handlingMethod)) {
                final var violations = forExecutables().validateParameters(resource, handlingMethod, args);
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

            // Continue if the methodDescriptor is not null, has constrained return value, and method must be validated
            if (methodDescriptor != null
                    && methodDescriptor.hasConstrainedReturnValue()
                    && mustValidateMethod(resource.getClass(), resourceMethod.getDefinitionMethod(), handlingMethod)) {
                final var violations = forExecutables().validateReturnValue(resource, handlingMethod, result);
                constraintViolations.addAll(violations);
                // In case the result is a Response, validate the contained entity also
                if (result instanceof Response) {
                    final var entityViolations = forExecutables()
                            .validateReturnValue(resource, handlingMethod, ((Response) result).getEntity());
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
     * Wrapper method for {@link ValidateOnExecutionHandler#mustValidateMethod(Class, Method, Method)}
     *
     * @param clazz            The {@link Class} on which the method will be invoked.
     * @param method           The {@link Method} to be examined.
     * @param validationMethod {@link Method} used for cache.
     * @return {@code true} if the method should be validated, {@code false} otherwise.
     */
    private boolean mustValidateMethod(final Class<?> clazz, final Method method, final Method validationMethod) {
        return validateOnExecutionHandler.mustValidateMethod(clazz, method, validationMethod);
    }
}
