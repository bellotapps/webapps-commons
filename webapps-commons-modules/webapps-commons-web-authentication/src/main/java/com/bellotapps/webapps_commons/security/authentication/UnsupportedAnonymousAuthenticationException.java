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

package com.bellotapps.webapps_commons.security.authentication;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception to be thrown in case the authentication is not present, but the authentication is mandatory.
 */
/* package */ class UnsupportedAnonymousAuthenticationException extends AuthenticationException {

    /**
     * Default constructor.
     */
    /* package */ UnsupportedAnonymousAuthenticationException() {
        super("Authentication is not optional");
    }
}
