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

import com.bellotapps.webapps_commons.security.authorization.Grant;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An extension of an {@link AbstractAuthenticationToken}.
 */
/* package */ class AuthenticationTokenAdapter extends AbstractAuthenticationToken {

    /**
     * The username of the user that this token belongs to.
     * Will be considered the principal in this token.
     */
    private final String username;

    /**
     * Constructor.
     *
     * @param username The username of the user that this token belongs to. Will be considered the principal in this token.
     * @param grants   A {@link Set} of {@link Grant}s
     *                 that will be assigned to this {@link org.springframework.security.core.Authentication}.
     */
    /* package */ AuthenticationTokenAdapter(final String username, final Collection<Grant> grants) {
        super(grants.stream().map(Grant::asString).map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
        this.username = username;
    }

    /**
     * Authenticates this token.
     */
    /* package */ void authenticate() {
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return (CredentialsContainer) () -> {
            // Do nothing
        };
    }

    @Override
    public Object getPrincipal() {
        Assert.state(isAuthenticated(), "Not yet authenticated");
        return username;
    }

    @Override
    public void setAuthenticated(final boolean authenticated) {
        Assert.state(authenticated || !isAuthenticated(), "Can't undo authentication");
        super.setAuthenticated(authenticated);
    }
}
