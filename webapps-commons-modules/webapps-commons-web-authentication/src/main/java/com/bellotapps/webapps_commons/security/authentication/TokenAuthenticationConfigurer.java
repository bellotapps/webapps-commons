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

import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ErrorHandlerFactory;
import com.bellotapps.webapps_commons.data_transfer.json.ApiObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * Configuration class, which defines several beans to be used by the token authentication mechanism.
 * An {@link ApiObjectMapper}, an {@link ErrorHandler}, and a {@link AuthenticationTokenDataProvider} beans
 * must be defined the same context.
 *
 * @see ErrorHandler
 * @see ApiObjectMapper
 * @see AuthenticationTokenDataProvider
 * @see com.bellotapps.utils.error_handler.EnableErrorHandler
 * @see EnableTokenAuthentication
 * @see AbstractWebSecurityConfig
 */
@Configuration
public class TokenAuthenticationConfigurer implements BeanFactoryAware, BeanClassLoaderAware {

    private final static Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationConfigurer.class);

    /**
     * The {@link BeanFactory} used to build an {@link ErrorHandlerFactory}.
     */
    private BeanFactory beanFactory;

    /**
     * The {@link ClassLoader} used to build an {@link ErrorHandlerFactory}.
     */
    private ClassLoader classLoader;

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanClassLoader(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiObjectMapper mockedApiObjectMapper() {
        LOGGER.warn("No ApiObjectMapper bean found in context. Using mocked mapper.");
        return new ApiObjectMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorHandler mockedErrorHandler() {
        Assert.state(beanFactory != null && classLoader != null, "Missing BeanFactory or ClassLoader");
        LOGGER.warn("No ErrorHandler found in context. " +
                "Using mocked handler, " +
                "which uses only the error handlers defined by the webapp-commons-error-handlers module.");
        return new ErrorHandlerFactory(classLoader, beanFactory)
                .createErrorHandler("com.bellotapps.webapps_commons.error_handlers");
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationTokenDataProvider mockedTokenDataProvider() {
        LOGGER.warn("No AuthenticationTokenDataProvider found in context. " +
                "Using mocked provider, which will not hand-in any useful data from the raw token.");
        return rawToken -> AuthenticationConstants.DUMMY_TOKEN_DATA;
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenAuthenticationProvider tokenAuthenticationProvider(final AuthenticationTokenDataProvider provider) {
        return new TokenAuthenticationProvider(provider);
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenAuthenticationFilter tokenAuthenticationFilter(final TokenAuthenticationFailureHandler handler) {
        return new TokenAuthenticationFilter(handler);
    }

    @Bean
    @Autowired
    @ConditionalOnMissingBean
    public TokenAuthenticationFailureHandler tokenAuthenticationFailureHandler(final ErrorHandler errorHandler,
                                                                               final ApiObjectMapper objectMapper) {
        return new TokenAuthenticationFailureHandler(errorHandler, objectMapper);
    }
}
