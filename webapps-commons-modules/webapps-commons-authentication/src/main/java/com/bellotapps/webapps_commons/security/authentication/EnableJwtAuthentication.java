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

import com.bellotapps.webapps_commons.security.authorization.GrantsProvider;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enables the jwt authentication system, by importing needed beans into the scope.
 * In order to be used, the "com.bellotapps.webapps-commons.authentication.jwt.publicKey" property must be set,
 * and {@link AuthenticationTokenBlacklistedChecker} and {@link GrantsProvider} beans
 * must exist in the application context
 * (if they are not defined, the application will boot but won't work as expected).
 *
 * @see JwtAuthenticationConfigurer
 * @see AuthenticationProperties.JwtProperties
 * @see AuthenticationTokenDecoder
 * @see JwtAuthenticationTokenDecoder
 * @see AuthenticationTokenDataProvider
 * @see JwtAuthenticationTokenDataProvider
 * @see AuthenticationTokenBlacklistedChecker
 * @see GrantsProvider
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(JwtAuthenticationConfigurer.class)
public @interface EnableJwtAuthentication {
}
