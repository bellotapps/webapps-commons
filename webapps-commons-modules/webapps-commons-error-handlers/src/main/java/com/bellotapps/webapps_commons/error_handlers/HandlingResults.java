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

package com.bellotapps.webapps_commons.error_handlers;

import com.bellotapps.utils.error_handler.HandlingResult;
import com.bellotapps.webapps_commons.constants.HttpStatus;
import com.bellotapps.webapps_commons.web.dtos.api_errors.IllegalParamValueErrorDto;
import com.bellotapps.webapps_commons.web.dtos.api_errors.RepresentationErrorDto;
import com.bellotapps.webapps_commons.web.dtos.api_errors.ServerErrorDto;

import java.util.List;

/**
 * Class containing several {@link HandlingResult}s constants and methods to be reused.
 */
/* package */ class HandlingResults {

    // ================================================================================================================
    // Static results
    // ================================================================================================================

    /**
     * {@link HandlingResult} to be sent when a bad representation error (i.e malformed json, missing property, etc.)
     * is performed by the client.
     */
    /* package */ static final HandlingResult<RepresentationErrorDto> BAD_REPRESENTATION = HandlingResult
            .withPayload(HttpStatus.BAD_REQUEST.getCode(), RepresentationErrorDto.REPRESENTATION_ERROR_DTO);


    /**
     * {@link HandlingResult} to be sent when an unauthorized action is tried to be performed.
     */
    /* package */ static final HandlingResult<Void> UNAUTHORIZED =
            HandlingResult.justErrorCode(HttpStatus.FORBIDDEN.getCode());

    /**
     * {@link HandlingResult} to be sent when authentication is missing,
     * and the action to be performed needs the author to be authenticated.
     * Note that the status code to be returned is {@link HttpStatus#UNAUTHORIZED},
     * whose real meaning is "Unauthenticated".
     */
    /* package */ static final HandlingResult<Void> UNAUTHENTICATED =
            HandlingResult.justErrorCode(HttpStatus.UNAUTHORIZED.getCode());

    /**
     * {@link HandlingResult} to be sent when trying to access an entity that is not present
     * (e.g access a non-existence user's profile, or requesting the address of a non-existence shopping order).
     */
    /* package */ static final HandlingResult<Void> NOT_FOUND =
            HandlingResult.justErrorCode(HttpStatus.NOT_FOUND.getCode());


    /**
     * {@link HandlingResult} to be sent when something went wrong with the application
     * (an unexpected exception was thrown).
     */
    /* package */ static final HandlingResult<ServerErrorDto> SERVER_ERROR = HandlingResult
            .withPayload(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), ServerErrorDto.BASIC_SERVER_ERROR_DTO);

    /**
     * {@link HandlingResult} to be sent when an external service (i.e web service, database, etc.) is not available.
     */
    /* package */ static final HandlingResult<ServerErrorDto> SERVICE_UNAVAILABLE = HandlingResult
            .withPayload(HttpStatus.SERVICE_UNAVAILABLE.getCode(), ServerErrorDto.SERVICE_UNAVAILABLE_ERROR_DTO);


    // ================================================================================================================
    // Dynamic results
    // ================================================================================================================

    /**
     * Creates a {@link HandlingResult} to be sent when illegal param values are received,
     * which includes a {@link HttpStatus#BAD_REQUEST} status code, including an {@link IllegalParamValueErrorDto}
     * with the list of params that contain illegal values.
     *
     * @return The {@link HandlingResult}.
     */
    /* package */
    static HandlingResult<IllegalParamValueErrorDto> illegalParams(final List<String> illegalParams) {
        final var dto = new IllegalParamValueErrorDto(illegalParams);
        return HandlingResult.withPayload(HttpStatus.BAD_REQUEST.getCode(), dto);
    }
}
