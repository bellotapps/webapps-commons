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

import java.util.Optional;

/**
 * Concrete implementation of {@link AuthenticationTokenDataProvider}.
 */
public class JwtAuthenticationTokenDataProvider implements AuthenticationTokenDataProvider {

    /**
     * An {@link AuthenticationTokenDecoder} in charge of decoding a raw token.
     */
    private final AuthenticationTokenDecoder authenticationTokenDecoder;

    /**
     * An {@link AuthenticationTokenBlacklistedChecker} in charge of checking if the token is not blacklisted.
     */
    private final AuthenticationTokenBlacklistedChecker authenticationTokenBlacklistedChecker;

    /**
     * Constructor.
     *
     * @param authenticationTokenDecoder            An {@link AuthenticationTokenDecoder}
     *                                              in charge of decoding a raw token.
     * @param authenticationTokenBlacklistedChecker An {@link AuthenticationTokenBlacklistedChecker}
     *                                              in charge of checking if the token is not blacklisted.
     */
    public JwtAuthenticationTokenDataProvider(
            final AuthenticationTokenDecoder authenticationTokenDecoder,
            final AuthenticationTokenBlacklistedChecker authenticationTokenBlacklistedChecker) {
        this.authenticationTokenDecoder = authenticationTokenDecoder;
        this.authenticationTokenBlacklistedChecker = authenticationTokenBlacklistedChecker;
    }

    @Override
    public TokenData provide(final String encodedToken) throws TokenException {
        // If it is not null, and is not blacklisted, then provide.
        // Else if null, or if it is blacklisted, then throw a TokenException
        return Optional.ofNullable(authenticationTokenDecoder.decode(encodedToken))
                .filter(tokenData -> !authenticationTokenBlacklistedChecker.isBlacklisted(tokenData.getId()))
                .orElseThrow(() -> new TokenException("Blacklisted token"));
    }
}
