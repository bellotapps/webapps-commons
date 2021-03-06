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
 * Defines behaviour for an object in charge of providing all the grants in the application.
 */
public interface GrantsProvider {

    /**
     * Builds a {@link Grant} from its {@link String} representation.
     *
     * @param string The {@link Grant}'s {@link String} representation.
     * @return The {@link Grant} that corresponds to the given {@code string}.
     */
    Optional<Grant> fromString(final String string);
}
