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

import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.HandlingResult;
import com.bellotapps.webapps_commons.data_transfer.json.ApiObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bellotapps.webapps_commons.security.authentication.AuthenticationConstants.AUTHENTICATION_SCHEME;


/**
 * {@link AuthenticationFailureHandler} in charge of translating {@link AuthenticationException} into error responses.
 * It delegates the responsability to an {@link ErrorHandler}
 */
public class TokenAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * Indicates the type of data being returned in the response by this filter.
     */
    private static final String CONTENT_TYPE = "application/json";

    /**
     * Indicates the header that will include the applicable authentication schemes supported by the system
     */
    private static final String APPLICABLE_AUTHENTICATION_SCHEMES_HEADER = "WWW-Authenticate";


    /**
     * The error handler in charge of handling exceptions.
     */
    private final ErrorHandler errorHandler;

    /**
     * {@link ApiObjectMapper} used to map results into JSON objects in the response body.
     */
    private final ApiObjectMapper objectMapper;

    /**
     * Constructor.
     *
     * @param errorHandler The error handler in charge of handling exceptions.
     * @param objectMapper {@link ApiObjectMapper} used to map results into JSON objects in the response body.
     */
    public TokenAuthenticationFailureHandler(final ErrorHandler errorHandler, final ApiObjectMapper objectMapper) {
        this.errorHandler = errorHandler;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final AuthenticationException authException)
            throws IOException {
        // Check if the authException is not a FailedJwtAuthenticationException,
        // in which case we must extract the original JwtException
        final var throwable = authException instanceof FailedTokenAuthenticationException ?
                ((FailedTokenAuthenticationException) authException).getOriginalTokenException() : authException;
        // Ask the ErrorHandler to handle the exception
        final var result = errorHandler.handle(throwable);
        // Set up response with data to be sent to the client
        setUpResponse(response, result);
    }

    /**
     * Sets up the given {@code response} in order to send the authentication error to the client.
     *
     * @param response The {@link HttpServletResponse} to be set up.
     * @param result   The {@link HandlingResult} containing the data to be sent to the client.
     * @throws IOException In case the {@link ApiObjectMapper} could not write the value in the response body.
     */
    private <T> void setUpResponse(final HttpServletResponse response, final HandlingResult<T> result)
            throws IOException {
        response.setStatus(result.getHttpErrorCode());
        response.addHeader(APPLICABLE_AUTHENTICATION_SCHEMES_HEADER, AUTHENTICATION_SCHEME);
        final var entity = result.getErrorRepresentationEntity();
        if (entity == null) {
            return;
        }
        response.setContentType(CONTENT_TYPE);
        objectMapper.writeValue(response.getOutputStream(), entity);
    }
}
