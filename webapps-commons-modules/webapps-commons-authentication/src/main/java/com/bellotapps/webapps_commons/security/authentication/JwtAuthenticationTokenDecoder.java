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
import com.bellotapps.webapps_commons.security.authorization.GrantsProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.util.StringUtils;

import java.security.PublicKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bellotapps.webapps_commons.security.authentication.JwtAuthenticationTokenConstants.ROLES_CLAIM_NAME;

/**
 * Concrete implementation of {@link AuthenticationTokenDecoder}, using jwt tokens.
 */
public class JwtAuthenticationTokenDecoder implements AuthenticationTokenDecoder {

    /**
     * The {@link PublicKey} used to verify the jwt token signature.
     */
    private final PublicKey publicKey;

    /**
     * A {@link JwtHandlerAdapter} used to handle the decoding process.
     */
    private final JwtHandlerAdapter<Jws<Claims>> jwtHandlerAdapter;


    /**
     * Constructor.
     *
     * @param grantsProvider A {@link GrantsProvider} used to translate {@link String}s into {@link Grant}s.
     * @param publicKey      The {@link PublicKey} used to verify the jwt token signature.
     */
    public JwtAuthenticationTokenDecoder(final PublicKey publicKey, final GrantsProvider grantsProvider) {
        this.publicKey = publicKey;
        this.jwtHandlerAdapter = new CustomJwtHandlerAdapter(grantsProvider);
    }


    @Override
    public TokenData decode(final String encodedToken) throws TokenException {
        if (!StringUtils.hasText(encodedToken)) {
            throw new IllegalArgumentException("The token must not be null or empty");
        }
        try {
            final var claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parse(encodedToken, jwtHandlerAdapter)
                    .getBody();


            // Previous step validated the following values
            final var tokenId = Long.parseLong(claims.getId());
            final var username = claims.getSubject();
            @SuppressWarnings("unchecked") final var roles = (List<Grant>) claims.get(ROLES_CLAIM_NAME);

            return new TokenData(tokenId, username, roles);

        } catch (final MalformedJwtException | SignatureException | ExpiredJwtException | UnsupportedJwtException
                | MissingClaimException e) {
            throw new TokenDecodingException("There was a problem with the jwt token", e);
        }
    }


    /**
     * Custom implementation of {@link JwtHandlerAdapter}.
     */
    private static class CustomJwtHandlerAdapter extends JwtHandlerAdapter<Jws<Claims>> {

        /**
         * Constructor.
         *
         * @param grantsProvider A {@link GrantsProvider} used to translate {@link String}s into {@link Grant}s.
         */
        private CustomJwtHandlerAdapter(final GrantsProvider grantsProvider) {
            this.grantsProvider = grantsProvider;
        }

        /**
         * A {@link GrantsProvider} used to translate {@link String}s into {@link Grant}s.
         */
        private final GrantsProvider grantsProvider;


        @Override
        public Jws<Claims> onClaimsJws(final Jws<Claims> jws) {
            final var header = jws.getHeader();
            final var claims = jws.getBody();

            // Check jti is not missing
            final var jtiString = claims.getId();
            if (!StringUtils.hasText(jtiString)) {
                throw new MissingClaimException(header, claims, "Missing \"jwt id\" claim");
            }
            // Check if the jtiString is a long
            try {
                Long.parseLong(jtiString);
            } catch (final NumberFormatException e) {
                throw new MalformedJwtException("The \"jwt id\" claim must be an integer or a long", e);
            }

            // Check roles is not missing
            final var rolesObject = claims.get(ROLES_CLAIM_NAME);
            if (rolesObject == null) {
                throw new MissingClaimException(header, claims, "Missing \"roles\" claim");
            }
            // Check roles is a Collection
            if (!(rolesObject instanceof Collection)) {
                throw new MalformedJwtException("The \"roles\" claim must be a collection");
            }
            // Transform the collection into a Set of Grants
            @SuppressWarnings("unchecked") final var roles = ((Collection<String>) rolesObject).stream()
                    .map(grantsProvider::fromString) // Transform the string into a Grant
                    .filter(Optional::isPresent) // If the provider could not decode the grant, then discard
                    .map(Optional::get) // Unwrap the grant from the Optional
                    .collect(Collectors.toList());
            claims.put(ROLES_CLAIM_NAME, roles);

            // Check issued at date is present and it is not a future date
            final var issuedAt = Optional.ofNullable(claims.getIssuedAt())
                    .orElseThrow(() ->
                            new MissingClaimException(header, claims, "Missing \"issued at\" date"));
            if (issuedAt.after(new Date())) {
                throw new MalformedJwtException("The \"issued at\" date is a future date");
            }
            // Check expiration date is not missing
            if (claims.getExpiration() == null) {
                throw new MissingClaimException(header, claims, "Missing \"expiration\" date");
            }

            return jws;
        }
    }
}
