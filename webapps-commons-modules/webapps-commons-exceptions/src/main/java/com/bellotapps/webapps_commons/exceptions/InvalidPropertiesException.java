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

package com.bellotapps.webapps_commons.exceptions;

import java.util.List;

/**
 * {@link RuntimeException} thrown when querying, trying to apply filters to properties that does not exist.
 */
public class InvalidPropertiesException extends RuntimeException {

    /**
     * A {@link List} holding those properties that does not exist.
     */
    private final List<String> invalidProperties;

    /**
     * Default constructor.
     *
     * @param invalidProperties A {@link List} holding those properties that does not exist.
     */
    public InvalidPropertiesException(List<String> invalidProperties) {
        super();
        this.invalidProperties = invalidProperties;
    }

    /**
     * Constructor which can set a {@code message}.
     *
     * @param message           The detail message,
     *                          which is saved for later retrieval by the {@link #getMessage()} method.
     * @param invalidProperties A {@link List} holding those properties that does not exist.
     */
    public InvalidPropertiesException(String message, List<String> invalidProperties) {
        super(message);
        this.invalidProperties = invalidProperties;
    }

    /**
     * Constructor which can set a {@code message} and a {@code cause}.
     *
     * @param message           The detail message,
     *                          which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause             The cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                          For more information, see {@link RuntimeException#RuntimeException(Throwable)}.
     * @param invalidProperties A {@link List} holding those properties that does not exist.
     */
    public InvalidPropertiesException(String message, Throwable cause, List<String> invalidProperties) {
        super(message, cause);
        this.invalidProperties = invalidProperties;
    }

    /**
     * @return The {@link List} holding those properties that does not exist.
     */
    public List<String> getInvalidProperties() {
        return invalidProperties;
    }
}
