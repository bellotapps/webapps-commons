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
 * Defines behaviour of the service in charge of providing a {@link TokenData} instance,
 * built from a raw token (represented by a {@link String}).
 */
@FunctionalInterface
public interface AuthenticationTokenDataProvider {

    /**
     * Retrieves {@link TokenData} from a {@link String} representation of it,
     * throwing a {@link TokenException} in case the raw token is not valid
     * (i.e malformation, expired, blacklisted, etc).
     *
     * @param encodedToken The encoded token.
     * @return {@link TokenData} taken from the given raw token.
     * @throws TokenException In case the token is not valid.
     */
    TokenData provide(final String encodedToken) throws TokenException;
}
