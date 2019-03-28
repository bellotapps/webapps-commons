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

package com.bellotapps.webapps_commons.config;


import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;
import com.bellotapps.webapps_commons.data_transfer.json.ApiObjectMapper;
import com.bellotapps.webapps_commons.error_handling.UncaughtExceptionHandlerFilter;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Enables the Jersey application configuration, creating several beans for this kind of apps.
 *
 * @see JerseyConfig
 * @see ThrowableMapper
 * @see ApiObjectMapper
 * @see UncaughtExceptionHandlerFilter
 * @see ExceptionHandler
 * @see ErrorHandler
 * @see ExceptionHandlerObject
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(value = {
        JerseyApplicationConfigurer.class,
        ErrorHandlerConfigurer.class,
})
public @interface EnableJerseyApplication {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation declarations e.g.:
     * {@code @EnableErrorHandler("org.my.pkg")} instead of {@code @EnableErrorHandler(basePackages="org.my.pkg")}.
     *
     * @return The same as {@link #basePackages()}.
     */
    @AliasFor(annotation = EnableJerseyApplication.class, value = "basePackages")
    String[] value() default {};

    /**
     * Base packages to scan for classes annotated with {@link javax.ws.rs.ext.Provider}.
     * {@link #value()} is an alias for (and mutually exclusive with) this
     * attribute. Use {@link #basePackageClasses()} for a type-safe alternative to String-based package names.
     *
     * @return The packages names set in the annotation as base packages.
     */
    @AliasFor(annotation = EnableJerseyApplication.class, value = "value")
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()}
     * for specifying the packages to scan for classes annotated with {@link javax.ws.rs.ext.Provider}.
     * The package of each class specified will be scanned.
     * Consider creating a special no-op marker class or interface in
     * each package that serves no purpose other than being referenced by this attribute.
     *
     * @return The classes set in the annotation as base package classes.
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * Base packages to scan for {@link com.bellotapps.utils.error_handler.ExceptionHandler}s
     *
     * @return The packages where {@link ExceptionHandler}s must be founded.
     */
    String[] errorHandlersPackages() default {};

    /**
     * Base packages to scan for {@link com.bellotapps.utils.error_handler.ExceptionHandler}s,
     * using the type-safe approach
     *
     * @return The classes whose packages must be search for {@link ExceptionHandler}s.
     */
    Class<?>[] errorHandlersPackagesClasses() default {};
}
