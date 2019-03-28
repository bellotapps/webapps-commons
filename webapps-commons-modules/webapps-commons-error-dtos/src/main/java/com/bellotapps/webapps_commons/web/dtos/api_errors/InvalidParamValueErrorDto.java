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
 * Data transfer object for client errors caused when setting an invalid value to a param (path or query).
 */
public class InvalidParamValueErrorDto extends ClientErrorDto {

    /**
     * The parameters whose value is invalid.
     */
    private final String conflictingParam; // Jersey only checks one parameter

    /**
     * Constructor.
     *
     * @param conflictingParam The parameters whose value is invalid.
     */
    public InvalidParamValueErrorDto(final String conflictingParam) {
        super(ErrorFamily.INVALID_PARAM_VALUE);
        this.conflictingParam = conflictingParam;
    }

    /**
     * @return The parameters whose value is invalid.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getConflictingParam() {
        return conflictingParam;
    }
}
