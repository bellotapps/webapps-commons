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

package com.bellotapps.webapps_commons.web.dtos.api_errors;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data transfer object for errors returned by the API.
 */
public abstract class ApiErrorDto {

    /**
     * The {@link ErrorKind} (i.e Client or Server error).
     */
    private final ErrorKind errorKind;

    /**
     * A human-readable description of the error kind.
     */
    private final String errorKindDescription;

    /**
     * A human readable message for the person consuming the API.
     */
    private final String message;

    /**
     * Constructor.
     *
     * @param errorKind The {@link ErrorKind} (i.e Client or Server error).
     * @param message   A human readable message for the person consuming the API.
     */
    public ApiErrorDto(final ErrorKind errorKind, String message) {
        this.errorKind = errorKind;
        this.errorKindDescription = errorKind.getDescription();
        this.message = message;
    }

    /**
     * @return The {@link ErrorKind} (i.e Client or Server error).
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public ErrorKind getErrorKind() {
        return errorKind;
    }

    /**
     * @return A human-readable description of the error kind.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getErrorKindDescription() {
        return errorKindDescription;
    }

    /**
     * @return A human readable message for the person consuming the API.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getMessage() {
        return message;
    }

    /**
     * Enum containing kind of errors the system can reach.
     */
    public enum ErrorKind {
        CLIENT {
            @Override
            public String getDescription() {
                return "Client side error";
            }
        },
        SERVER {
            @Override
            protected String getDescription() {
                return "Server side error";
            }
        };

        /**
         * @return A human-readable description of the error kind.
         */
        protected abstract String getDescription();
    }
}
