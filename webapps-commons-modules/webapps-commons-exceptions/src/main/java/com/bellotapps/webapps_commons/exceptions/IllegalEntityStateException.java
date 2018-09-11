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

import com.bellotapps.webapps_commons.errors.IllegalEntityStateError;

/**
 * {@link RuntimeException} thrown when trying to perform an operation over an entity
 * when the state of it is invalid for that operation.
 */
public class IllegalEntityStateException extends RuntimeException {

    /**
     * The {@link IllegalEntityStateError} that caused this exception to be thrown.
     */
    private final IllegalEntityStateError error;

    /**
     * Default constructor.
     *
     * @param error The {@link IllegalEntityStateError} that caused this exception to be thrown.
     */
    public IllegalEntityStateException(IllegalEntityStateError error) {
        super();
        this.error = error;
    }

    /**
     * Constructor which can set a {@code message}.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param error   The {@link IllegalEntityStateError} that caused this exception to be thrown.
     */
    public IllegalEntityStateException(IllegalEntityStateError error, String message) {
        super(message);
        this.error = error;
    }

    /**
     * Constructor which can set a {@code message} and a {@code cause}.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause   The cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                For more information, see {@link RuntimeException#RuntimeException(Throwable)}.
     * @param error   The {@link IllegalEntityStateError} that caused this exception to be thrown.
     */
    public IllegalEntityStateException(IllegalEntityStateError error, String message, Throwable cause) {
        super(message, cause);
        this.error = error;
    }

    /**
     * @return The {@link IllegalEntityStateError} that caused this exception to be thrown.
     */
    public IllegalEntityStateError getError() {
        return error;
    }
}
