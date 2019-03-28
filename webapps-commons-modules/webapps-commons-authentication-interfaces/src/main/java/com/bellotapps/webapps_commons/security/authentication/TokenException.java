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

package com.bellotapps.webapps_commons.security.authentication;

import com.bellotapps.webapps_commons.exceptions.UnauthenticatedException;

/**
 * Exception to be thrown when there is any problem with a token (i.e decoding, invalid, blacklisted, etc.).
 */
public class TokenException extends UnauthenticatedException {

    /**
     * Default constructor.
     */
    public TokenException() {
        super();
    }

    /**
     * Constructor which can set a {@code message}.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public TokenException(final String message) {
        super(message);
    }

    /**
     * Constructor which can set a mes{@code message} and a {@code cause}.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause   The cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                For more information, see {@link RuntimeException#RuntimeException(Throwable)}.
     */
    public TokenException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
