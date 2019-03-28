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

package com.bellotapps.webapps_commons.config;

import com.bellotapps.utils.error_handler.AnnotationErrorHandlerCreationConfigurer;
import com.bellotapps.utils.error_handler.EnableErrorHandlerFactory;
import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ErrorHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A configuration class to configure the {@link ErrorHandler} using {@link EnableJerseyApplication} annotation.
 */
@Configuration
@EnableErrorHandlerFactory
public class ErrorHandlerConfigurer extends AnnotationErrorHandlerCreationConfigurer<EnableJerseyApplication> {

    /**
     * Constructor.
     *
     * @param errorHandlerFactory The {@link ErrorHandlerFactory} to be used to create the {@link ErrorHandler} bean.
     */
    @Autowired
    public ErrorHandlerConfigurer(final ErrorHandlerFactory errorHandlerFactory) {
        super(errorHandlerFactory);
    }

    @Override
    protected Collection<String> getPackagesCollectionFromAnnotation(final EnableJerseyApplication annotation) {
        final var basePackagesStream = Arrays.stream(annotation.errorHandlersPackages());
        final var basePackageClassesStream = Arrays.stream(annotation.errorHandlersPackagesClasses())
                .map(Class::getPackage)
                .map(Package::getName);
        return Stream.concat(basePackagesStream, basePackageClassesStream).collect(Collectors.toSet());
    }

    @Override
    protected Class<EnableJerseyApplication> getAnnotationClass() {
        return EnableJerseyApplication.class;
    }
}
