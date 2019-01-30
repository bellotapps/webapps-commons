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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Jersey configuration class, an extension of {@link ResourceConfig}.
 */
public class JerseyConfig extends ResourceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(JerseyConfig.class);

    /**
     * Constructor.
     *
     * @param objectMapper    The {@link ObjectMapper} to be used by Jersey.
     * @param throwableMapper The {@link ThrowableMapper} to be used by Jersey,
     *                        in order to bypass all {@link Throwable}s
     *                        to the {@link com.bellotapps.utils.error_handler.ErrorHandler}.
     * @param packages        Packages to be scanned for Jersey {@link Provider}s.
     */
    public JerseyConfig(final ObjectMapper objectMapper,
                        final ThrowableMapper throwableMapper, final String... packages) {
        // Register packages with resources and providers
        registerPackages(packages);
        // Register ObjectMapper which will be used to serialize/deserialize JSON
        register(new JacksonJaxbJsonProvider(objectMapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
        // Register the ThrowableMapper that will wire the exception into the error handler
        register(throwableMapper);
        // Disable Bean Validation
        property(ServerProperties.BV_FEATURE_DISABLE, true);
    }

    /**
     * Registers the classes annotated with the {@link Provider} annotation in the given {@code packages}.
     * This allows package scanning with Jersey (as currently not supported by library).
     *
     * @param packages The packages containing providers.
     */
    private void registerPackages(final String... packages) {
        // First, initialize the scanner that will be used to scan the packages.
        final var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Provider.class));

        // Then search for classes that are providers in the given packages.
        final var classesNames = Arrays.stream(packages)
                .map(scanner::findCandidateComponents)
                .flatMap(Collection::stream)
                .map(BeanDefinition::getBeanClassName)
                .collect(Collectors.toList());

        // Some class names might be null (according to the documentation).
        if (classesNames.stream().anyMatch(Objects::isNull)) {
            LOGGER.warn("Come Jersey Providers could not be initialized as the class name could not be retrieved.");
        }

        // Finally, register those classes that are not null
        classesNames.stream()
                .filter(Objects::nonNull)
                .map(className -> ClassUtils.resolveClassName(className, this.getClassLoader()))
                .forEach(this::register);
    }
}
