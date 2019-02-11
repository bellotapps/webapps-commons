/*
 * Copyright 2019 BellotApps
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
 * Defines behavior for an object that can encode a token into a {@link String}.
 */
public interface AuthenticationTokenEncoder {

    /**
     * Transforms a {@link TokenData} into a {@link String} representation of it.
     *
     * @param token The token to be encoded.
     * @return An encoded representation of a token.
     */
    String encode(final TokenData token);
}
