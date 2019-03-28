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

import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

/**
 * {@link AuthenticationException} thrown when there are token issues
 * (e.g invalid token, expired token, blacklisted, etc).
 * <p>
 * This exception acts as a wrapper of {@link TokenException},
 * to be used in the spring security exception handling mechanism.
 */
/* package */ class FailedTokenAuthenticationException extends AuthenticationException {

    /**
     * Constructor which can set a {@code cause}.
     *
     * @param cause The original {@link TokenException} thrown that caused this exception to be created.
     */
    /* package */ FailedTokenAuthenticationException(final TokenException cause) {
        this("", cause);
    }

    /**
     * Constructor which can set a mes{@code message} and a {@code cause}.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause   The original {@link TokenException} thrown that caused this exception to be created.
     */
    /* package */ FailedTokenAuthenticationException(final String message, final TokenException cause) {
        super(message, cause);
        Assert.notNull(cause, "The TokenException must not be null");
    }

    /**
     * @return The original {@link TokenException} thrown that caused this exception to be created.
     */
    /* package */ TokenException getOriginalTokenException() {
        return (TokenException) this.getCause();
    }
}
