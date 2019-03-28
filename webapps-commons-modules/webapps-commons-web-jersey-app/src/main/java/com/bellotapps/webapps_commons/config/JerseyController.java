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

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import javax.ws.rs.ext.Provider;
import java.lang.annotation.*;

/**
 * Indicates that a class is a Jersey Controller.
 * <p>
 * This annotation serves as a specialization of {@link Component},
 * allowing for implementation classes to be autodetected through classpath scanning,
 * and {@link Provider}, allowing annotated classes to be detected for registering by package registration by Jersey.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Provider
@Component
public @interface JerseyController {

    /**
     * Alias for {@link Component#value()} attribute.
     *
     * @return The same as {@link Component#value()}.
     */
    @AliasFor(annotation = Component.class, attribute = "value")
    String value() default "";
}
