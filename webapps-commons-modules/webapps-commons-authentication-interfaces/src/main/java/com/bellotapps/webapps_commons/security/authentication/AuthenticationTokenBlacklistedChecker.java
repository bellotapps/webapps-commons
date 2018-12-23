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
 * Defines behaviour for an object that can tell if a token is blacklisted (i.e not valid) given its id.
 */
@FunctionalInterface
public interface AuthenticationTokenBlacklistedChecker {

    /**
     * Indicates whether a token is blacklisted (i.e not valid).
     *
     * @param tokenId The id of the token to be checked.
     * @return {@code true} if the token is blacklisted, or {@code false} otherwise.
     */
    boolean isBlacklisted(final long tokenId);
}
