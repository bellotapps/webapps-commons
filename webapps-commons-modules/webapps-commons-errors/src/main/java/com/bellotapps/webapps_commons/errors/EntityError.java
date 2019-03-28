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

package com.bellotapps.webapps_commons.errors;

/**
 * Class representing an error that occurs when trying to create or update an entity wrongly
 * (i.e unique violations, not present references or constraint validations).
 */
public abstract class EntityError {

    /**
     * A human-readable message to be used for debugging purposes.
     */
    private final String message;

    /**
     * Constructor.
     *
     * @param message A human-readable message to be used for debugging purposes.
     */
    public EntityError(final String message) {
        this.message = message;
    }

    /**
     * @return A human-readable message to be used for debugging purposes.
     */
    public String getMessage() {
        return message;
    }
}
