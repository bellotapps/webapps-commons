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

import java.util.Collections;

/**
 * Class containing constants to be used by the authentication module.
 */
/* package */ class AuthenticationConstants {

    /**
     * Indicates which HTTP header includes the authentication credentials.
     */
    /* package */ static final String AUTHENTICATION_HEADER = "Authorization";

    /**
     * Indicates the authentication scheme supported by the system.
     */
    /* package */ static final String AUTHENTICATION_SCHEME = "Bearer";

    /**
     * A {@link TokenData} that cannot be used for anything (except for processing unauthenticated requests).
     */
    /* package */ static final TokenData DUMMY_TOKEN_DATA =
            new TokenData(-1, "", Collections.unmodifiableList(Collections.emptyList()));
}
