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

package com.bellotapps.webapps_commons.security.authorization;

import org.springframework.util.Assert;

/**
 * A simple {@link Grant}s enum containing roles (user and admin).
 */
public enum RoleGrant implements Grant {

    /**
     * Indicates a user is a normal user.
     */
    ROLE_USER,
    /**
     * Indicates a user is an administrator.
     */
    ROLE_ADMIN;

    @Override
    public String asString() {
        return super.toString().replace("_", "-").toLowerCase();
    }

    @Override
    public String toString() {
        return asString();
    }

    /**
     * Retrieves the {@link RoleGrant} that matches the given {@code value}.
     * It applies the inverse operation performed in the {@link #asString()} method.
     *
     * @param value The {@link String} representation belonging to the returned {@link RoleGrant}.
     * @return The {@link RoleGrant} with the given {@code value} {@link String} representation.
     * @throws IllegalArgumentException If there is no {@link RoleGrant} with the given {@code value}.
     */
    public static RoleGrant fromString(final String value) throws IllegalArgumentException {
        Assert.notNull(value, "The value must be provided");
        return valueOf(value.replace("-", "_").toUpperCase());
    }
}
