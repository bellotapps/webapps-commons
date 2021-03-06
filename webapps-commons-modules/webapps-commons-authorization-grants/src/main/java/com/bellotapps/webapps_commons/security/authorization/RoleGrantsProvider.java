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

package com.bellotapps.webapps_commons.security.authorization;

import java.util.Optional;

/**
 * A {@link GrantsProvider} for the {@link RoleGrant} enum.
 */
public class RoleGrantsProvider implements GrantsProvider {

    @Override
    public Optional<Grant> fromString(final String string) {
        try {
            return Optional.of(RoleGrant.fromString(string)); // The RoleGrant#fromString method should not return null.
        } catch (final IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }
}
