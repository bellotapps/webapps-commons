/*
 * Copyright 2018 BellotApps
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

package com.bellotapps.webapps_commons.errors;

/**
 * Class representing an error that occurs when referencing an entity that does not exist.
 * (This reference can be through one or more values).
 */
public final class NotPresentReferenceError extends MultiFieldError {

    /**
     * Constructor.
     *
     * @param message     A human-readable message to be used for debugging purposes.
     * @param errorFields The group of fields that altogether trigger the error.
     */
    public NotPresentReferenceError(final String message, final String... errorFields) {
        super(message, errorFields);
    }
}
