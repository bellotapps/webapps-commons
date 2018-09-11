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

import java.util.List;

/**
 * Data transfer object for client errors caused when setting an illegal value to a param (path or query).
 */
public class IllegalParamValueErrorDto extends ClientErrorDto {

    /**
     * A {@link List} containing those parameters with illegal values.
     */
    private final List<String> conflictingParams;

    /**
     * Constructor.
     *
     * @param conflictingParams A {@link List} containing those parameters with illegal values.
     */
    public IllegalParamValueErrorDto(final List<String> conflictingParams) {
        super(ErrorFamily.ILLEGAL_PARAM_VALUE);
        this.conflictingParams = conflictingParams;
    }

    /**
     * @return A {@link List} containing those parameters with illegal values.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public List<String> getConflictingParams() {
        return conflictingParams;
    }
}
