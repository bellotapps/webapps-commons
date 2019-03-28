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

import com.bellotapps.webapps_commons.security.authorization.Grant;
import io.jsonwebtoken.Jwts;
import org.springframework.util.Assert;

import java.security.PrivateKey;
import java.sql.Date;
import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Concrete implementation of {@link AuthenticationTokenEncoder}.
 */
public class JwtAuthenticationTokenEncoder implements AuthenticationTokenEncoder {

    /**
     * The private key used to sign tokens.
     */
    private final PrivateKey privateKey;

    /**
     * The duration of tokens, in seconds.
     */
    private final long duration;

    /**
     * Constructor.
     *
     * @param privateKey The private key used to sign tokens.
     * @param duration   The duration of tokens, in seconds.
     */
    public JwtAuthenticationTokenEncoder(final PrivateKey privateKey, final long duration) {
        this.privateKey = privateKey;
        this.duration = duration;
    }

    @Override
    public String encode(final TokenData token) {
        Assert.notNull(token, "The token must not be null");

        final var now = Instant.now();
        final var expiration = now.plusSeconds(duration);
        final var grants = token.getGrants().stream().map(Grant::asString).collect(Collectors.toList());
        return Jwts.builder()
                .setId(Long.toString(token.getId()))
                .setSubject(token.getUsername())
                .claim(JwtAuthenticationTokenConstants.ROLES_CLAIM_NAME, grants)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(privateKey, JwtAuthenticationTokenConstants.SIGNATURE_ALGORITHM)
                .compact()
                ;
    }
}
