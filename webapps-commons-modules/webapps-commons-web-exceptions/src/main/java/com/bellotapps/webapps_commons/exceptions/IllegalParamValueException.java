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

package com.bellotapps.webapps_commons.exceptions;

import java.util.List;

/**
 * A {@link RuntimeException} that represents an illegal parameter value error.
 */
public class IllegalParamValueException extends RuntimeException {

    /**
     * A {@link List} containing all param names that have been set
     * with the illegal values that caused this exception to be thrown.
     */
    private final List<String> conflictingParams;

    /**
     * Default constructor.
     *
     * @param conflictingParams A {@link List} containing all param names that have been set
     *                          with the illegal values that caused this exception to be thrown.
     */
    public IllegalParamValueException(final List<String> conflictingParams) {
        super();
        this.conflictingParams = conflictingParams;
    }

    /**
     * Constructor which can set a {@code message}.
     *
     * @param message           The detail message,
     *                          which is saved for later retrieval by the {@link #getMessage()} method.
     * @param conflictingParams A {@link List} containing all param names that have been set
     *                          with the illegal values that caused this exception to be thrown.
     */
    public IllegalParamValueException(final String message, final List<String> conflictingParams) {
        super(message);
        this.conflictingParams = conflictingParams;
    }

    /**
     * @param message           The detail message,
     *                          which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause             The cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                          For more information, see {@link RuntimeException#RuntimeException(Throwable)}.
     * @param conflictingParams A {@link List} containing all param names that have been set
     *                          with the illegal values that caused this exception to be thrown.
     */
    public IllegalParamValueException(final String message,
                                      final Throwable cause, final List<String> conflictingParams) {
        super(message, cause);
        this.conflictingParams = conflictingParams;
    }

    /**
     * @return A {@link List} containing all param names that have been set
     * with the illegal values that caused this exception to be thrown.
     */
    public List<String> getConflictingParams() {
        return conflictingParams;
    }
}
