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

package com.bellotapps.webapps_commons.validation.aspects;

import com.bellotapps.webapps_commons.exceptions.CustomConstraintViolationException;
import com.bellotapps.webapps_commons.validation.annotations.ValidateConstraintsAfter;
import com.bellotapps.webapps_commons.validation.annotations.ValidateConstraintsBefore;
import org.aspectj.lang.Aspects;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.validation.Validator;

/**
 * An aspect that adds auto-constraints-validation to objects whose methods or constructors are annotated with
 * {@link ValidateConstraintsBefore} or {@link ValidateConstraintsAfter} annotations.
 */
@Aspect
public class ConstraintValidationAspect implements InitializingBean {

    /**
     * A {@link Validator} instance used to validate the object.
     * Note: The {@link Autowired} annotation is set here because this bean is created
     * using the {@link Aspects#aspectOf(Class)}, which uses the default constructor.
     */
    @Autowired
    private final Validator validator;

    /**
     * Default constructor used by the AspectJ configuration.
     */
    private ConstraintValidationAspect() {
        this.validator = null;
    }

    @Override
    public void afterPropertiesSet() throws IllegalStateException {
        // After initialization, check that the validator was set (if not, throw an IllegalStateException).
        Assert.state(validator != null, "The validator has not been initialized");
    }

    /**
     * A {@link Pointcut} to match anything annotated with the {@link ValidateConstraintsBefore} annotation.
     */
    @Pointcut("@annotation(com.bellotapps.webapps_commons.validation.annotations.ValidateConstraintsBefore)")
    private void validateAnnotatedBefore() {
    }

    /**
     * A {@link Pointcut} to match anything annotated with the {@link ValidateConstraintsAfter} annotation.
     */
    @Pointcut("@annotation(com.bellotapps.webapps_commons.validation.annotations.ValidateConstraintsAfter)")
    private void validateAnnotatedAfter() {
    }

    /**
     * A {@link Pointcut} to match any constructor execution.
     */
    @Pointcut("execution(*.new(..))")
    private void anyConstructor() {
    }

    /**
     * A {@link Pointcut} to match any method execution.
     */
    @Pointcut("execution(* *(..))")
    private void anyMethod() {
    }

    /**
     * A {@link Pointcut} to match any method execution or constructor execution
     * when they are annotated with the {@link ValidateConstraintsBefore} annotation.
     */
    @Pointcut("validateAnnotatedBefore() && (anyMethod() || anyConstructor())")
    private void validateBeforePointcut() {
    }

    /**
     * A {@link Pointcut} to match any method execution or constructor execution
     * when they are annotated with the {@link ValidateConstraintsAfter} annotation.
     */
    @Pointcut("validateAnnotatedAfter() && (anyMethod() || anyConstructor())")
    private void validateAfterPointcut() {
    }

    /**
     * Performs a validation to the object being targeted before the execution of the method annotated with the
     * {@link ValidateConstraintsBefore} annotation.
     *
     * @param joinPoint The {@link JoinPoint} in which this aspect is being called.
     * @throws CustomConstraintViolationException If the target object violates a constraint.
     */
    @Before("validateBeforePointcut()")
    public void validateBefore(final JoinPoint joinPoint) throws CustomConstraintViolationException {
        validate(joinPoint);
    }

    /**
     * Performs a validation to the object being targeted after the execution of the method or constructor with the
     * {@link ValidateConstraintsAfter} annotation.
     *
     * @param joinPoint The {@link JoinPoint} in which this aspect is being called.
     * @throws CustomConstraintViolationException If the target object violates a constraint.
     */
    @After("validateAfterPointcut()")
    public void validateAfter(final JoinPoint joinPoint) throws CustomConstraintViolationException {
        validate(joinPoint);
    }

    /**
     * Performs the validation.
     *
     * @param joinPoint The {@link JoinPoint} in which this aspect is being called.
     * @throws CustomConstraintViolationException If the target object violates a constraint.
     */
    private void validate(final JoinPoint joinPoint) throws CustomConstraintViolationException {
        Assert.state(validator != null, "The validator has not been initialized");
        Assert.notNull(joinPoint, "The join point must not be null");
        final var validatedObject = joinPoint.getTarget();
        final var violations = validator.validate(validatedObject);
        if (!violations.isEmpty()) {
            throw new CustomConstraintViolationException(violations);
        }
    }
}
