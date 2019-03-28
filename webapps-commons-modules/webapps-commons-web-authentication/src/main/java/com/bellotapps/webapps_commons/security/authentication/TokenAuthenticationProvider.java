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


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * {@link AuthenticationProvider} in charge of performing token authentication.
 */
public final class TokenAuthenticationProvider implements AuthenticationProvider {

    /**
     * The {@link AuthenticationTokenDataProvider} that will provide a {@link TokenData} instance from a raw token.
     */
    private final AuthenticationTokenDataProvider authenticationTokenDataProvider;

    /**
     * Constructor.
     *
     * @param authenticationTokenDataProvider The {@link AuthenticationTokenDataProvider}
     *                                        that will provide a {@link TokenData} instance from a raw token.
     */
    public TokenAuthenticationProvider(final AuthenticationTokenDataProvider authenticationTokenDataProvider) {
        this.authenticationTokenDataProvider = authenticationTokenDataProvider;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "The authentication must not be null");
        Assert.isInstanceOf(RawAuthenticationToken.class, authentication,
                "The authentication must be a RawAuthenticationToken");

        final var rawAuthenticationToken = (RawAuthenticationToken) authentication;
        try {
            // Throws TokenException in case the token is not valid
            final var tokenData = authenticationTokenDataProvider.provide(rawAuthenticationToken.getToken());
            // We create a new token with the needed data (username, roles, etc.)
            final var resultToken = new AuthenticationTokenAdapter(tokenData.getUsername(), tokenData.getGrants());
            resultToken.authenticate(); // Then, we mark the token as authenticated 
            return resultToken;
        } catch (final TokenException e) {
            throw new FailedTokenAuthenticationException(e);
        }
    }

    @Override
    public boolean supports(final Class<?> authenticationClass) {
        return ClassUtils.isAssignable(RawAuthenticationToken.class, authenticationClass);
    }
}
