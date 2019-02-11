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

import com.bellotapps.webapps_commons.core.FrameworkConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Configuration properties for the authentication module.
 */
@ConfigurationProperties(prefix = AuthenticationProperties.PREFIX)
public class AuthenticationProperties {

    /**
     * Prefix for the "authentication" group.
     */
    /* package */ static final String PREFIX = FrameworkConstants.MAIN_PROPERTIES_PREFIX + ".authentication";

    /**
     * The "jwt" group.
     */
    @NestedConfigurationProperty
    private JwtProperties jwt;


    /**
     * Getter for the "jwt" group.
     *
     * @return The {@link JwtProperties}.
     */
    public JwtProperties getJwt() {
        return jwt;
    }

    /**
     * Setter for the "jwt" group.
     *
     * @param jwt The {@link JwtProperties}.
     */
    public void setJwt(final JwtProperties jwt) {
        this.jwt = jwt;
    }

    /**
     * Sub-Configuration properties for JWTs.
     */
    public static final class JwtProperties {

        /**
         * The public key.
         */
        private String publicKey;
        /**
         * The private key.
         */
        private String privateKey;
        /**
         * The duration of the JWT.
         */
        private Long duration;

        /**
         * Getter for the public key.
         *
         * @return The public key.
         */
        public String getPublicKey() {
            return publicKey;
        }

        /**
         * Getter for the private key.
         *
         * @return The public key.
         */
        public String getPrivateKey() {
            return privateKey;
        }

        /**
         * Getter for the duration.
         *
         * @return The duration of the JWT.
         */
        public Long getDuration() {
            return duration;
        }

        /**
         * Setter for the public key.
         *
         * @param publicKey The public key.
         */
        public void setPublicKey(final String publicKey) {
            this.publicKey = publicKey;
        }

        /**
         * Setter for the private key.
         *
         * @param privateKey The private key.
         */
        public void setPrivateKey(final String privateKey) {
            this.privateKey = privateKey;
        }

        /**
         * Setter for the duration.
         *
         * @param duration The duration of the JWT.
         */
        public void setDuration(final Long duration) {
            this.duration = duration;
        }
    }
}
