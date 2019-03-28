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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data transfer object for API errors that are caused by the client consuming it.
 */
public abstract class ClientErrorDto extends ApiErrorDto {

    /**
     * The {@link ClientErrorDto.ErrorFamily}.
     */
    private final ErrorFamily errorFamily;

    /**
     * A human-readable description of the client error family.
     */
    private final String errorFamilyDescription;


    /**
     * Constructor.
     *
     * @param errorFamily The {@link ClientErrorDto.ErrorFamily}.
     */
    public ClientErrorDto(final ErrorFamily errorFamily) {
        super(ErrorKind.CLIENT, errorFamily.getMessage());
        this.errorFamily = errorFamily;
        this.errorFamilyDescription = errorFamily.getDescription();
    }

    /**
     * @return The {@link ClientErrorDto.ErrorFamily}.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public ErrorFamily getErrorFamily() {
        return errorFamily;
    }

    /**
     * @return A human-readable description of the client error family.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getErrorFamilyDescription() {
        return errorFamilyDescription;
    }

    /**
     * Enum containing the family of client errors.
     */
    public enum ErrorFamily {
        /**
         * Indicates that a given parameter (path, query, etc.) value is not valid
         * (e.g sending alphabetic characters when an integer is expected).
         */
        INVALID_PARAM_VALUE {
            @Override
            protected String getDescription() {
                return "Invalid Parameter Value";
            }

            @Override
            protected String getMessage() {
                return "There were invalid parameter (path or query) errors";
            }
        },
        /**
         * Indicates that a given parameter (path, query, etc.) value is not legal
         * (e.g sending a negative number when a positive integer is expected).
         */
        ILLEGAL_PARAM_VALUE {
            @Override
            protected String getDescription() {
                return "Illegal Parameter Value";
            }

            @Override
            protected String getMessage() {
                return "There were illegal parameter (path or query) errors";
            }
        },
        /**
         * Indicates that a sent entity can't be interpreted as it should be.
         * Examples of this errors are malformed JSONs (e.g a JSON missing double quotes for field names),
         * missing required properties, unknown properties were included,
         * or invalid formats (e.g sending a string when a number is expected).
         */
        REPRESENTATION {
            @Override
            protected String getDescription() {
                return "Entity Representation Error";
            }

            @Override
            protected String getMessage() {
                return "There were entity representation errors";
            }
        },
        /**
         * Indicates that a referenced entity does not exists (i.e similar to a foreign key constraint violation).
         */
        NOT_PRESENT_REFERENCE {
            @Override
            protected String getDescription() {
                return "Entities Not Present Error";
            }

            @Override
            protected String getMessage() {
                return "There were not present entities references errors";
            }
        },
        /**
         * Indicates that a value (or group of values) that must be unique, and already exist, was sent in an entity
         * (e.g if the email must be unique for each user, this error is thrown when sending an already used email).
         */
        UNIQUE_VIOLATION {
            @Override
            protected String getDescription() {
                return "Unique Violation Error";
            }

            @Override
            protected String getMessage() {
                return "There were unique value (or group of values) violation errors";
            }
        },
        /**
         * Indicates that there were validation problems when creating or updating an entity.
         * They can be missing values for mandatory fields (e.g username for users),
         * or illegal values (e.g future dates for a birth date).
         */
        CONSTRAINT_VIOLATION {
            @Override
            protected String getDescription() {
                return "Constraint Violation Errors";
            }

            @Override
            protected String getMessage() {
                return "There were constraint violation errors when creating or updating an entity";
            }
        },
        /**
         * Indicates that an operation over an entity was tried to be performed when the state of it was invalid
         * for the operation to be performed).
         */
        ILLEGAL_STATE {
            @Override
            protected String getDescription() {
                return "Illegal State Error";
            }

            @Override
            protected String getMessage() {
                return "An operation was tried to be performed over an entity " +
                        "when its state was not valid for the operation to be performed.";
            }
        };

        /**
         * @return A human-readable description of the client error family.
         */
        protected abstract String getDescription();

        /**
         * @return A human readable message for the person consuming the API.
         */
        protected abstract String getMessage();
    }
}
