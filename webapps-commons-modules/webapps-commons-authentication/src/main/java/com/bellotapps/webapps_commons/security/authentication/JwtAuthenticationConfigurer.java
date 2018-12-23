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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Configuration class for the jwt authentication system.
 * In order to be used, an {@link AuthenticationTokenBlacklistedChecker} and a {@link GrantsProvider} beans
 * must be defined within the scope,
 * and the "com.adtomiclabs.commons.authentication.jwt.key.public" property must be set.
 */
@Configuration
public class JwtAuthenticationConfigurer {

    /**
     * The {@link Logger} object.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationConfigurer.class);

    @Bean
    @ConditionalOnMissingBean
    public KeyFactory keyFactory() throws NoSuchAlgorithmException {
        return KeyFactory.getInstance(JwtAuthenticationTokenConstants.KEY_FACTORY_ALGORITHM);
    }

    @Bean
    @Autowired
    @ConditionalOnMissingBean
    public AuthenticationTokenDecoder authenticationTokenDecoder(
            final KeyFactory keyFactory,
            final GrantsProvider grantsProvider,
            @Value("${com.adtomiclabs.commons.authentication.jwt.key.public}") final String publicKeyString)
            throws InvalidKeySpecException {
        final X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64Utils.decodeFromString(publicKeyString));
        final PublicKey publicKey = keyFactory.generatePublic(keySpecX509);
        return new JwtAuthenticationTokenDecoder(grantsProvider, publicKey);
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
