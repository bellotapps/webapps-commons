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
 * Class representing an error that occurs when operating over an entity in an invalid state
 * (to perform that operation).
 */
public class IllegalEntityStateError extends MultiFieldError {

    /**
     * Constructor.
     *
     * @param message                A human-readable message to be used for debugging purposes.
     * @param illegalStateProperties The properties of the entity that have illegal state.
     */
    public IllegalEntityStateError(final String message, final String... illegalStateProperties) {
        super(message, illegalStateProperties);
    }
}