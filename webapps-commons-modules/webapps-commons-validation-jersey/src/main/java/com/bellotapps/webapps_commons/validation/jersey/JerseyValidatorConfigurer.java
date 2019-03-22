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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;


/**
 * Configuration class for Jersey Validation
 *
 * @see EnableJerseyValidation
 * @see JerseyValidator
 * @see org.glassfish.jersey.server.internal.inject.ConfiguredValidator
 * @see javax.validation.Validator
 * @see javax.validation.ValidatorFactory
 * @see javax.validation.Configuration
 */
@Configuration
public class JerseyValidatorConfigurer {

    /**
     * A {@link Logger} to notify bean creation events.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(JerseyValidatorConfigurer.class);


    /**
     * Creates a {@link javax.validation.Configuration} bean if no other bean of this type is in the scope.
     *
     * @return A default {@link javax.validation.Configuration}.
     */
    @Bean
    @ConditionalOnMissingBean
    public javax.validation.Configuration configuration() {
        LOGGER.info("Using default validation configuration");
        return Validation.byDefaultProvider().configure();
    }

    /**
     * Creates a {@link ValidatorFactory} bean if no other bean of this type is in the scope.
     *
     * @param configuration A {@link javax.validation.Configuration} from which the {@link ValidatorFactory} is created.
     * @return The created {@link ValidatorFactory}.
     */
    @Bean
    @ConditionalOnMissingBean
    public ValidatorFactory validatorFactory(final javax.validation.Configuration configuration) {
        LOGGER.info("Using default validator factory");
        return configuration.buildValidatorFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ConstraintViolationExceptionCreator defaultConstraintViolationExceptionSupplier() {
        return ConstraintViolationException::new;
    }

    /**
     * Creates an instance of a {@link JerseyValidator}.
     *
     * @param configuration                       The {@link javax.validation.Configuration}
     *                                            that indicates whether executable validation should be performed.
     * @param validatorFactory                    The {@link ValidatorFactory}
     *                                            from which the delegate {@link javax.validation.Validator}
     *                                            for the {@link JerseyValidator} is built.
     * @param constraintViolationExceptionCreator The {@link ConstraintViolationExceptionCreator} used by the
     *                                            {@link JerseyValidator}.
     * @return The created {@link JerseyValidator}.
     */
    @Bean
    public ConfiguredValidator jerseyValidator(
            final javax.validation.Configuration configuration,
            final ValidatorFactory validatorFactory,
            final ConstraintViolationExceptionCreator constraintViolationExceptionCreator) {
        return new JerseyValidator(
                validatorFactory.getValidator(),
                configuration.getBootstrapConfiguration().isExecutableValidationEnabled(),
                constraintViolationExceptionCreator);
    }
}
