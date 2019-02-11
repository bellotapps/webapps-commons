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
import org.springframework.util.Base64Utils;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Configuration class for the jwt authentication system.
 * In order to be used, an {@link AuthenticationTokenBlacklistedChecker} and a {@link GrantsProvider} beans
 * must be defined within the scope,
 * and the "com.adtomiclabs.commons.authentication.jwt.key.public" property must be set.
 */
@Configuration
@EnableConfigurationProperties(AuthenticationProperties.class)
public class JwtAuthenticationConfigurer {

    /**
     * The {@link Logger} object.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationConfigurer.class);

    /**
     * {@link AuthenticationProperties} to be used when creating the authentication module beans.
     */
    private final AuthenticationProperties authenticationProperties;

    @Autowired
    public JwtAuthenticationConfigurer(final AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public KeyFactory keyFactory() throws NoSuchAlgorithmException {
        return KeyFactory.getInstance(JwtAuthenticationTokenConstants.KEY_FACTORY_ALGORITHM);
    }

    @Bean
    @Autowired
    @ConditionalOnMissingBean
    public AuthenticationTokenEncoder authenticationTokenEncoder(final KeyFactory keyFactory)
            throws InvalidKeySpecException {
        final var encodedPrivateKeyString = authenticationProperties.getJwt().getPrivateKey();
        final var privateKeyString = Base64Utils.decodeFromString(encodedPrivateKeyString);
        final var privateKeySpec = new PKCS8EncodedKeySpec(privateKeyString);
        final var privateKey = keyFactory.generatePrivate(privateKeySpec);
        final var duration = authenticationProperties.getJwt().getDuration();
        return new JwtAuthenticationTokenEncoder(privateKey, duration);
    }

    @Bean
    @Autowired
    @ConditionalOnMissingBean
    public AuthenticationTokenDecoder authenticationTokenDecoder(
            final KeyFactory keyFactory,
            final GrantsProvider grantsProvider)
            throws InvalidKeySpecException {

        final var encodedPublicKeyString = authenticationProperties.getJwt().getPublicKey();
        final var publicKeyString = Base64Utils.decodeFromString(encodedPublicKeyString);
        final var publicKeySpec = new X509EncodedKeySpec(publicKeyString);
        final var publicKey = keyFactory.generatePublic(publicKeySpec);
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
