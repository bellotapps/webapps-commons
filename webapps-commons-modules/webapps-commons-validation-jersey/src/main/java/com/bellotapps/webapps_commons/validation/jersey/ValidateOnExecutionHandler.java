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

import java.lang.reflect.Method;

/**
 * Defines behaviour for an object than can handle the
 * {@link javax.validation.executable.ValidateOnExecution} annotation
 * when defining whether a method should be validated.
 */
public interface ValidateOnExecutionHandler {

    /**
     * Determine whether the given resource {@code method} to-be-executed on the given {@code klass} should be
     * validated.
     *
     * @param clazz            The {@link Class} on which the method will be invoked.
     * @param method           The {@link Method} to be examined.
     * @param validationMethod {@link Method} used for cache.
     * @return {@code true} if the method should be validated, {@code false} otherwise.
     */
    boolean mustValidateMethod(final Class<?> clazz, final Method method, final Method validationMethod);
}
