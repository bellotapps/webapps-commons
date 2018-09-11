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


import com.bellotapps.webapps_commons.errors.IllegalEntityStateError;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data transfer object for client errors caused when an operation over an entity is not valid  because of its state.
 */
public class IllegalEntityStateErrorDto extends ClientErrorDto {

    /**
     * The {@link IllegalEntityStateError}.
     */
    private final IllegalEntityStateError error;

    /**
     * Constructor.
     *
     * @param error The {@link IllegalEntityStateError}.
     */
    public IllegalEntityStateErrorDto(final IllegalEntityStateError error) {
        super(ErrorFamily.ILLEGAL_STATE);
        this.error = error;
    }

    /**
     * @return The {@link IllegalEntityStateError}.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public IllegalEntityStateError getError() {
        return error;
    }
}
