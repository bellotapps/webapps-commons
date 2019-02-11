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

import com.bellotapps.webapps_commons.security.authorization.GrantsProvider;
import com.bellotapps.webapps_commons.security.authorization.RoleGrantsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.Base64Utils;

import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;

/**
 * Configuration class for the jwt authentication system.
 * In order to be used, the "com.bellotapps.webapps-commons.authentication.jwt.publicKey" property must be set,
 * and {@link AuthenticationTokenBlacklistedChecker} and {@link GrantsProvider} beans
 * must exist in the application context
 * (if they are not defined, the application will boot but won't work as expected).
 *
 * @see EnableJwtAuthentication
 */
@Configuration
@EnableConfigurationProperties(AuthenticationProperties.class)
@Import(CoreJwtConfigurer.class)
public class JwtAuthenticationConfigurer extends AbstractJwtConfigurer {

    /**
     * The {@link Logger} object.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationConfigurer.class);

    @Autowired
    public JwtAuthenticationConfigurer(final AuthenticationProperties authenticationProperties) {
        super(authenticationProperties.getJwt());
    }

    @Bean
    @Autowired
    @ConditionalOnMissingBean
    public AuthenticationTokenDecoder authenticationTokenDecoder(final KeyFactory keyFactory,
                                                                 final GrantsProvider grantsProvider) {
        final var encodedPublicKeyString = Optional.ofNullable(getJwtProperties().getPublicKey())
                .orElseThrow(() -> {
                    LOGGER.error("A public key must be set!");
                    return new IllegalStateException("No public key found in the configuration properties");
                });
        final var publicKeyString = Base64Utils.decodeFromString(encodedPublicKeyString);
        final var publicKeySpec = new X509EncodedKeySpec(publicKeyString);
        final var publicKey = generateKey(keyFactory, publicKeySpec, KeyFactory::generatePublic);
        return new JwtAuthenticationTokenDecoder(publicKey, grantsProvider);
    }

    @Bean
    @Autowired
    @ConditionalOnMissingBean
    public AuthenticationTokenDataProvider authenticationTokenDataProvider(
            final AuthenticationTokenDecoder decoder,
            final AuthenticationTokenBlacklistedChecker checker) {
        return new JwtAuthenticationTokenDataProvider(decoder, checker);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationTokenBlacklistedChecker mockedChecker() {
        LOGGER.warn("No AuthenticationTokenBlacklistedChecker bean found. " +
                "Using mocked checker, which will state that all tokens are blacklisted. " +
                "Authentication won't work as expected");
        return id -> true; // This mocked checker is used to set up the context, but it MUST be overridden
    }

    @Bean
    @ConditionalOnMissingBean
    public GrantsProvider roleGrantsProvider() {
        LOGGER.warn("Using default grants provider, which is based on simple roles: user and admin");
        return new RoleGrantsProvider();
    }
}
