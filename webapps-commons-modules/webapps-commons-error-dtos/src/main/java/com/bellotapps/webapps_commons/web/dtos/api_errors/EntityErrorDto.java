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


import com.bellotapps.webapps_commons.errors.EntityError;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Data transfer object for client errors caused by trying to create or update and entity in a wrong way.
 */
public abstract class EntityErrorDto<T extends EntityError> extends ClientErrorDto {

    /**
     * The {@link List} of {@link EntityError}s.
     */
    private final List<T> errors;

    /**
     * Constructor.
     *
     * @param errorFamily The {@link ErrorFamily}.
     * @param errors      The {@link List} of {@link EntityError}s.
     */
    public EntityErrorDto(final ErrorFamily errorFamily, final List<T> errors) {
        super(errorFamily);
        this.errors = errors;
    }

    /**
     * @return The {@link List} of {@link EntityError}s.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public List<T> getErrors() {
        return errors;
    }
}
