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

package com.bellotapps.webapps_commons.validation.annotations;

import java.lang.annotation.*;

/**
 * Annotations used to mark methods or constructors in order to validate the annotated object state
 * according to the constraints set to it, performing the validation process after the method is executed.
 *
 * @see com.bellotapps.webapps_commons.validation.aspects.ConstraintValidationAspect
 * @see com.bellotapps.webapps_commons.validation.config.EnableValidationAspects
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateConstraintsAfter {
}
