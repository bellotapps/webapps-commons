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

package com.bellotapps.webapps_commons.web.dtos.api_errors;

/**
 * Data transfer object for API errors that are caused by the server.
 */
public class ServerErrorDto extends ApiErrorDto {

    /**
     * Constructor.
     *
     * @param message A human readable message for the person consuming the API.
     */
    private ServerErrorDto(final String message) {
        super(ErrorKind.SERVER, message);
    }

    /**
     * A {@link ServerErrorDto} to be sent when not being able to access external services.
     */
    public static final ServerErrorDto SERVICE_UNAVAILABLE_ERROR_DTO =
            new ServerErrorDto("The service is currently unavailable");

    /**
     * A {@link ServerErrorDto} to be sent when trying to invoke an operation that is not implemented yet.
     */
    public static final ServerErrorDto NOT_IMPLEMENTED_ERROR_DTO =
            new ServerErrorDto("The operation is not implemented yet.");

    /**
     * A {@link ServerErrorDto} to be sent when unexpected errors occur (i.e a non-caught exception).
     */
    public static final ServerErrorDto BASIC_SERVER_ERROR_DTO =
            new ServerErrorDto("There was an unexpected error");
}
