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

/**
 * Defines behaviour of the service in charge of managing authentication tokens.
 * Defines behavior for an object that can decode a token from a {@link String}.
 */
@FunctionalInterface
public interface AuthenticationTokenDecoder {

    /**
     * Retrieves {@link TokenData} from a {@link String} representation of it.
     *
     * @param encodedToken The encoded token.
     * @return {@link TokenData} taken from the given raw token.
     * @throws TokenException In case the token is not valid.
     */
    TokenData decode(final String encodedToken) throws TokenException;
}
