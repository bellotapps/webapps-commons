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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

/**
 * Abstract web security configuration class.
 */
public abstract class AbstractWebSecurityConfig extends WebSecurityConfigurerAdapter implements InitializingBean {

    /**
     * The {@link TokenAuthenticationFilter} to which the request will be delegated.
     */
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    /**
     * The {@link TokenAuthenticationProvider} which will perform the authentication step in the filter chain.
     */
    private final TokenAuthenticationProvider tokenAuthenticationProvider;

    /**
     * Constructor.
     *
     * @param tokenAuthenticationFilter   The {@link TokenAuthenticationFilter} to which the request will be delegated.
     * @param tokenAuthenticationProvider The {@link TokenAuthenticationProvider}
     *                                    which will perform the authentication step in the filter chain.
     */
    protected AbstractWebSecurityConfig(final TokenAuthenticationFilter tokenAuthenticationFilter,
                                        final TokenAuthenticationProvider tokenAuthenticationProvider) {
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.tokenAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
        this.tokenAuthenticationFilter.addOptionalAuthenticationMatcher(optionalAuthenticationMatchers());
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(tokenAuthenticationProvider);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .rememberMe().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Returns a {@link List} of {@link RequestMatcher}s that will be used to know if the request can be made
     * anonymously
     *
     * @return The {@link List} of {@link RequestMatcher}s.
     */
    protected abstract List<RequestMatcher> optionalAuthenticationMatchers();
}
