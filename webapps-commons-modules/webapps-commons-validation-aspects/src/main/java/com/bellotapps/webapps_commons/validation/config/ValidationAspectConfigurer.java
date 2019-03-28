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

package com.bellotapps.webapps_commons.validation.config;

import com.bellotapps.webapps_commons.validation.aspects.ConstraintValidationAspect;
import org.aspectj.lang.Aspects;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * Configuration class for the validation aspects.
 *
 * @see EnableValidationAspects
 * @see ConstraintValidationAspect
 */
@Configuration
public class ValidationAspectConfigurer {

    @Bean
    public ConstraintValidationAspect modelValidationAspect() {
        return Aspects.aspectOf(ConstraintValidationAspect.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
