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

import com.bellotapps.webapps_commons.security.authorization.Grant;

import java.util.Collections;
import java.util.List;

/**
 * A wrapper class that encapsulates information taken from a token.
 */
public final class TokenData {

    /**
     * The token's id.
     */
    private final long id;

    /**
     * The token's owner username.
     */
    private final String username;

    /**
     * The grants given to the token.
     */
    private final List<Grant> grants;

    /**
     * @param id       The token's id.
     * @param username The token's owner username.
     * @param grants   The grants given to the token.
     */
    public TokenData(final long id, final String username, final List<Grant> grants) {
        this.id = id;
        this.username = username;
        this.grants = Collections.unmodifiableList(grants); // This list cannot be changed.
    }

    /**
     * @return The token's id.
     */
    public long getId() {
        return id;
    }

    /**
     * @return The token's owner username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return The grants given to the token.
     */
    public List<Grant> getGrants() {
        return grants;
    }
}
