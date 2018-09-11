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

import com.bellotapps.webapps_commons.errors.UniqueViolationError;

import java.util.List;

/**
 * {@link RuntimeException} thrown when trying to use an already in use value (or group of values) that must be unique.
 */
public class UniqueViolationException extends RuntimeException {

    /**
     * A {@link List} containing all {@link UniqueViolationError}s that caused this exception to be thrown.
     */
    private final List<UniqueViolationError> errorList;

    /**
     * Default constructor.
     *
     * @param errorList A {@link List} containing all {@link UniqueViolationError}s that caused the exception to be thrown.
     */
    public UniqueViolationException(List<UniqueViolationError> errorList) {
        super();
        this.errorList = errorList;
    }

    /**
     * Constructor which can set a {@code message}.
     *
     * @param message   The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param errorList A {@link List} containing all {@link UniqueViolationError}s that caused the exception to be thrown.
     */
    public UniqueViolationException(String message, List<UniqueViolationError> errorList) {
        super(message);
        this.errorList = errorList;
    }

    /**
     * Constructor which can set a {@code message} and a {@code cause}.
     *
     * @param message   The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause     The cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                  For more information, see {@link RuntimeException#RuntimeException(Throwable)}.
     * @param errorList A {@link List} containing all {@link UniqueViolationError}s that caused the exception to be thrown.
     */
    public UniqueViolationException(String message, Throwable cause, List<UniqueViolationError> errorList) {
        super(message, cause);
        this.errorList = errorList;
    }

    /**
     * @return The {@link List} of {@link UniqueViolationError}s that caused this exception to be thrown.
     */
    public List<UniqueViolationError> getErrors() {
        return errorList;
    }
}
