/*
 * Copyright 2019 BellotApps
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
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Optional;

/**
 * Configuration class for the jwt authentication system.
 * In order to be used, the "com.bellotapps.webapps-commons.authentication.jwt.privateKey" and
 * "com.bellotapps.webapps-commons.authentication.jwt.duration" properties must be set.
 *
 * @see EnableJwtIssuer
 */
@Configuration
@EnableConfigurationProperties(AuthenticationProperties.class)
@Import(CoreJwtConfigurer.class)
public class JwtIssuerConfigurer extends AbstractJwtConfigurer {

    /**
     * The {@link Logger} object.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(JwtIssuerConfigurer.class);

    @Autowired
    public JwtIssuerConfigurer(final AuthenticationProperties authenticationProperties) {
        super(authenticationProperties.getJwt());
    }

    @Bean
    @Autowired
    @ConditionalOnMissingBean
    public AuthenticationTokenEncoder authenticationTokenEncoder(final KeyFactory keyFactory) {
        final var jwtProperties = getJwtProperties();
        final var encodedPrivateKeyString = Optional.ofNullable(jwtProperties.getPrivateKey())
                .orElseThrow(() -> {
                    LOGGER.error("A private key must be set!");
                    return new IllegalStateException("No private key found in the configuration properties");
                });
        final var duration = Optional.ofNullable(jwtProperties.getDuration())
                .orElseThrow(() -> {
                    LOGGER.error("A duration for the jwts must be set!");
                    return new IllegalStateException("No jwt duration found in the configuration properties");
                });
        final var privateKeyString = Base64Utils.decodeFromString(encodedPrivateKeyString);
        final var privateKeySpec = new PKCS8EncodedKeySpec(privateKeyString);
        final var privateKey = generateKey(keyFactory, privateKeySpec, KeyFactory::generatePrivate);
        return new JwtAuthenticationTokenEncoder(privateKey, duration);
    }
}
