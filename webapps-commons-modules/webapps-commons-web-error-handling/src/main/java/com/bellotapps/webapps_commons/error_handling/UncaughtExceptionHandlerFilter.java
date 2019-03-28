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

package com.bellotapps.webapps_commons.error_handling;


import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.webapps_commons.data_transfer.json.ApiObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

/**
 * Filter that is in charge of handling gracefully any uncaught exception.
 * Will return data in JSON format.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UncaughtExceptionHandlerFilter extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(UncaughtExceptionHandlerFilter.class);

    /**
     * Indicates the type of data being returned in the response by this filter.
     */
    private static final String CONTENT_TYPE = "application/json";

    /**
     * The {@link ErrorHandler} in charge of transforming an exception into data to be returned in the response.
     */
    private final ErrorHandler handler;

    /**
     * The {@link ApiObjectMapper} in charge of serializing data into JSON to be returned in the response body.
     */
    private final ApiObjectMapper apiObjectMapper;

    /**
     * Constructor.
     *
     * @param handler         The {@link ErrorHandler} to be used to handle errors by this filter.
     * @param apiObjectMapper The {@link ApiObjectMapper} to be used to transform responses into JSONs.
     */
    public UncaughtExceptionHandlerFilter(final ErrorHandler handler, final ApiObjectMapper apiObjectMapper) {
        this.handler = handler;
        this.apiObjectMapper = apiObjectMapper;
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse resp, final FilterChain chain) {
        try {
            chain.doFilter(req, resp);
        } catch (final Throwable exception) {
            LOGGER.debug("An exception was not caught and is being handled by the UncaughtExceptionFilter", exception);
            final var response = (HttpServletResponse) resp;
            if (response.isCommitted()) {
                LOGGER.error("Response was committed before handling exception");
                return;
            }
            try {
                final var container = handler.handle(exception);
                response.setStatus(container.getHttpErrorCode());
                Optional.ofNullable(container.getErrorRepresentationEntity())
                        .ifPresent(entity -> {
                            try {
                                response.setContentType(CONTENT_TYPE);
                                apiObjectMapper.writeValue(response.getOutputStream(), entity);
                            } catch (final IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        });
            } catch (final Throwable e) {
                LOGGER.error("Exception was thrown when handling exception!", e);
            }
        }
    }
}
