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

package com.bellotapps.webapps_commons.config;

//import com.adtomiclabs.commons.data_transfer.json.ApiObjectMapper;

import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.webapps_commons.data_transfer.json.ApiObjectMapper;
import com.bellotapps.webapps_commons.error_handling.UncaughtExceptionHandlerFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Configuration class for a Jersey application, which includes a {@link JerseyConfig} bean,
 * an {@link ObjectMapper} bean, a {@link ThrowableMapper} bean, and an {@link UncaughtExceptionHandlerFilter} bean.
 */
@Configuration
public class JerseyApplicationConfigurer implements ImportAware, InitializingBean {

    /**
     * The {@link Logger} object.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(JerseyApplicationConfigurer.class);

    /**
     * The {@link AnnotationMetadata} from where packages names will be taken for Jersey Providers package scan.
     */
    private AnnotationMetadata importMetadata;

    /**
     * The packages to be scanned by jersey in order to search for providers.
     */
    private String[] jerseyProviderPackages;

    /**
     * Flag indicating if this configuration class is initialized.
     */
    private boolean initialized;

    /**
     * Constructor.
     */
    public JerseyApplicationConfigurer() {
        this.jerseyProviderPackages = null;
        this.initialized = false;
    }

    @Override
    public void setImportMetadata(final AnnotationMetadata importMetadata) {
        this.importMetadata = importMetadata;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialize();
    }


    // ============================================================
    // Beans
    // ============================================================

    @Bean
    @ConditionalOnMissingBean
    public JerseyConfig jerseyConfig(final ApiObjectMapper apiObjectMapper, final ThrowableMapper throwableMapper) {
        if (!initialized || jerseyProviderPackages == null) {
            throw new IllegalStateException("The JerseyApplicationConfigurer was not correctly initialized");
        }
        return new JerseyConfig(apiObjectMapper, throwableMapper, jerseyProviderPackages);
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiObjectMapper apiObjectMapper() {
        if (!initialized) {
            throw new IllegalStateException("The JerseyApplicationConfigurer was not correctly initialized");
        }
        return new ApiObjectMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public ThrowableMapper throwableMapper(final ErrorHandler errorHandler) {
        return new ThrowableMapper(errorHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public UncaughtExceptionHandlerFilter uncaughtExceptionHandlerFilter(final ErrorHandler errorHandler,
                                                                         final ApiObjectMapper apiObjectMapper) {
        return new UncaughtExceptionHandlerFilter(errorHandler, apiObjectMapper);
    }


    // ============================================================
    // Helper methods
    // ============================================================

    /**
     * Initializes this configurer.
     *
     * @throws ClassNotFoundException In case the class name obtained from the {@code importMetadata} is not found.
     */
    private void initialize() throws ClassNotFoundException {

        final var importingClass = Class.forName(importMetadata.getClassName());
        final var enableJerseyApplication = Optional
                .ofNullable(AnnotationUtils.findAnnotation(importingClass, EnableJerseyApplication.class))
                .orElseThrow(IllegalArgumentException::new);
        this.jerseyProviderPackages = getPackages(enableJerseyApplication);
        LOGGER.debug("Will scan the following packages for Providers: {}", Arrays.asList(this.jerseyProviderPackages));

        this.initialized = true;
    }

    /**
     * Returns an array of packages names where jersey providers must be search for.
     *
     * @param enableJerseyApplication The {@link EnableJerseyApplication} annotation
     *                                from where packages names must be taken from.
     * @return The array of packages names.
     */
    private String[] getPackages(final EnableJerseyApplication enableJerseyApplication) {
        final var basePackagesStream = Arrays.stream(enableJerseyApplication.basePackages());
        final var basePackageClassesStream = Arrays.stream(enableJerseyApplication.basePackageClasses())
                .map(Class::getPackage)
                .map(Package::getName);
        return Stream.concat(basePackagesStream, basePackageClassesStream).toArray(String[]::new);
    }
}
