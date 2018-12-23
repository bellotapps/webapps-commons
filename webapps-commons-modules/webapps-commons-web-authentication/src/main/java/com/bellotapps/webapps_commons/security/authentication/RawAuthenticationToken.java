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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * An implementation of {@link Authentication} which does not support any of the methods of that interface.
 * It is used to pass a raw token (i.e a {@link String} representation of a token)
 * from a {@link TokenAuthenticationFilter} into a {@link TokenAuthenticationProvider}.
 */
/* package */ class RawAuthenticationToken implements Authentication {

    /**
     * The raw token wrapped by this class.
     */
    private final String token;

    /**
     * Constructor.
     *
     * @param token The raw token to be wrapped by this class.
     */
    /* package */ RawAuthenticationToken(final String token) {
        this.token = token;
    }

    /**
     * @return The raw token wrapped by this class.
     */
    /* package */ String getToken() {
        return token;
    }


    // ================================================================================
    // Authentication interface methods, which are not supported.
    // ================================================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }
}
