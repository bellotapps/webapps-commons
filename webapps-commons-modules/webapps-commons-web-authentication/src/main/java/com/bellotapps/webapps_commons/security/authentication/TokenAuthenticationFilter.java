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

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.bellotapps.webapps_commons.security.authentication.AuthenticationConstants.AUTHENTICATION_HEADER;
import static com.bellotapps.webapps_commons.security.authentication.AuthenticationConstants.AUTHENTICATION_SCHEME;

/**
 * {@link AbstractAuthenticationProcessingFilter} in charge of performing token authentication.
 */
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * A {@link List} of {@link RequestMatcher} for optional authentication requests.
     */
    private final List<RequestMatcher> optionalAuthenticationMatchers;

    /**
     * Constructor.
     *
     * @param tokenAuthenticationFailureHandler The {@link RequestMatcher} for the endpoint
     *                                          to which token validation is performed against to.
     */
    public TokenAuthenticationFilter(final TokenAuthenticationFailureHandler tokenAuthenticationFailureHandler) {
        super("/**");
        Assert.notNull(tokenAuthenticationFailureHandler, "A token authentication failure handler must be set");

        this.optionalAuthenticationMatchers = new LinkedList<>();
        this.setAuthenticationFailureHandler(tokenAuthenticationFailureHandler);
        this.setAuthenticationSuccessHandler((request, response, authentication) -> {
            // Do nothing
        });
    }

    /**
     * Adds all of the {@link RequestMatcher} in the given {@link List} of {@code matchers}
     * to the list of optional authentication matchers.
     *
     * @param matchers The {@link List} of {@link RequestMatcher} to be added.
     */
    public void addOptionalAuthenticationMatcher(final List<RequestMatcher> matchers) {
        optionalAuthenticationMatchers.addAll(matchers);
    }


    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
            throws AuthenticationException {
        final var authenticationManager = getAuthenticationManager();
        Assert.notNull(authenticationManager, "authenticationManager must be specified");

        // First, get the data from the header used to send authentication data.
        final var authorizationHeader = request.getHeader(AUTHENTICATION_HEADER);
        // If data is not present, then check if the request can be processed unauthenticated
        if (!StringUtils.hasText(authorizationHeader)) {
            // Anonymous request. Continue only if it matches the optionalAuthenticationMatcher
            if (matchesAny(request)) {
                return new AnonymousAuthenticationToken("ANONYMOUS", "ANONYMOUS",
                        Collections.singletonList(new SimpleGrantedAuthority("ANONYMOUS")));
            }
            throw new UnsupportedAnonymousAuthenticationException(); // Authentication is needed to process the request.
        }
        // If reached here, authentication credentials are present and, at least, the scheme (first word) is present
        final var credentials = authorizationHeader.split(" ");
        // We must check the scheme
        final var scheme = credentials[0];
        if (!AUTHENTICATION_SCHEME.equals(scheme)) {
            throw new UnsupportedAuthenticationSchemeException(AUTHENTICATION_HEADER); // Unsupported scheme
        }
        // If reached here, the scheme is supported. We must check the token is present
        if (credentials.length <= 1) {
            throw new MissingTokenException(); // As there is no more words than the scheme, then it is not present
        }
        // If reached here, the token is present. We assume everything is well formed (token is just one "word").
        final var rawToken = credentials[1];
        // Continue with the authentication process, and return its result 
        return authenticationManager.authenticate(new RawAuthenticationToken(rawToken));
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                            final FilterChain chain, final Authentication authResult)
            throws IOException, ServletException {
        // First, process request by parent
        super.successfulAuthentication(request, response, chain, authResult);
        // Then, continue with normal flow
        chain.doFilter(request, response);
    }

    @Override
    public void afterPropertiesSet() {
        // Do nothing (and avoid crashing because of the authenticationManager not being set during bean initialization)
    }

    /**
     * Checks if any of the optional {@link RequestMatcher} in the {@link #optionalAuthenticationMatchers}
     * matches the given {@code request}
     *
     * @param request The {@link HttpServletRequest} to be matched.
     * @return {@code true} if the request matches any of the optional {@link RequestMatcher}s,
     * or {@code false} otherwise.
     */
    private boolean matchesAny(final HttpServletRequest request) {
        return this.optionalAuthenticationMatchers.stream().anyMatch(matcher -> matcher.matches(request));
    }
}
