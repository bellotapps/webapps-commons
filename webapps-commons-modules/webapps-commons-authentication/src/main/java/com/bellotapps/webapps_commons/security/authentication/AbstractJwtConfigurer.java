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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Optional;

/**
 * An abstract jwt configurer where common jwt configuration stuff is defined.
 */
/* package */ abstract class AbstractJwtConfigurer {

    /**
     * The {@link Logger} object.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractJwtConfigurer.class);

    /**
     * Jwt configuration properties.
     */
    private final AuthenticationProperties.JwtProperties jwtProperties;


    /**
     * Constructor.
     *
     * @param jwtProperties The jwt configuration properties.
     */
    public AbstractJwtConfigurer(final AuthenticationProperties.JwtProperties jwtProperties) {
        this.jwtProperties = Optional.ofNullable(jwtProperties)
                .orElseThrow(() -> {
                    LOGGER.error("Jwt configuration must be set!");
                    return new IllegalStateException("No jwt configuration found in the configuration properties");
                });
    }

    /**
     * Jwt configuration properties getter.
     *
     * @return The Jwt configuration properties.
     */
    /* package */ AuthenticationProperties.JwtProperties getJwtProperties() {
        return jwtProperties;
    }

    /**
     * Generates a {@link Key} of type {@code K} from the given {@link KeySpec} of type {@code S},
     * using the given {@link KeyFactory}.
     *
     * @param keyFactory   The {@link KeyFactory} used to generate the {@link Key}.
     * @param keySpec      The {@link KeySpec} from where the {@link Key} will be created.
     * @param keyGenerator A {@link KeyGenerator}
     * @param <S>          The concrete type of {@link KeySpec}.
     * @param <K>          The concrete type of {@link Key}.
     * @return The generated {@link Key}.
     * @throws IllegalStateException If the key is invalid.
     */
    /* package */
    static <S extends KeySpec, K extends Key> K generateKey(final KeyFactory keyFactory, final S keySpec,
                                                            KeyGenerator<S, K> keyGenerator) {
        try {
            return keyGenerator.generateKey(keyFactory, keySpec);
        } catch (final InvalidKeySpecException e) {
            LOGGER.error("The key that was set is not valid!");
            throw new IllegalStateException("Invalid key", e);
        }
    }

    /**
     * Defines behaviour for an object that can generate {@link Key}s
     * from a {@link KeyFactory} and an {@link KeySpec}
     *
     * @param <S> The concrete type of {@link KeySpec}.
     * @param <K> The concrete type of {@link Key}.
     */
    @FunctionalInterface
    /* package */ interface KeyGenerator<S extends KeySpec, K extends Key> {

        /**
         * Generates a {@link Key} of type {@code K} using the given {@code keyFactory} and {@code keySpec}.
         *
         * @param keyFactory The {@link KeyFactory} used to generate the {@link Key}.
         * @param keySpec    The {@link KeySpec} from where the {@link Key} will be created.
         * @return The generated {@link Key}.
         */
        K generateKey(KeyFactory keyFactory, S keySpec) throws InvalidKeySpecException;
    }
}
