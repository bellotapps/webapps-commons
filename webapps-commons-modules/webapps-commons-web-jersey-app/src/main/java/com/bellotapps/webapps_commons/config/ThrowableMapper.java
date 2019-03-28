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

import com.bellotapps.utils.error_handler.ErrorHandler;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Optional;

/**
 * A global {@link ExceptionMapper} that is in charge of mapping any {@link Throwable} thrown
 * within a Jersey application, using an {@link ErrorHandler}.
 */
public class ThrowableMapper implements ExceptionMapper<Throwable> {

    /**
     * The {@link ErrorHandler} in charge of transforming an exception into data to be returned in the response.
     */
    private final ErrorHandler errorHandler;

    /**
     * Constructor.
     *
     * @param exceptionHandler The {@link ErrorHandler} to be used to map errors into responses.
     */
    public ThrowableMapper(final ErrorHandler exceptionHandler) {
        this.errorHandler = exceptionHandler;
    }

    @Override
    public Response toResponse(final Throwable exception) {
        final var result = errorHandler.handle(exception);
        return Response.status(result.getHttpErrorCode())
                .entity(Optional.ofNullable(result.getErrorRepresentationEntity()).orElse(""))
                .build();
    }
}
