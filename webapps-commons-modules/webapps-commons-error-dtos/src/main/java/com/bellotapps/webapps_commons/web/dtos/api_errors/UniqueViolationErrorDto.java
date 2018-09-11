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


import com.bellotapps.webapps_commons.errors.UniqueViolationError;

import java.util.List;

/**
 * Data transfer object for client errors caused by trying to set a value that is already used and must be unique.
 */
public final class UniqueViolationErrorDto extends EntityErrorDto<UniqueViolationError> {

    /**
     * @param errors The {@link List} of {@link UniqueViolationError}s.
     */
    public UniqueViolationErrorDto(final List<UniqueViolationError> errors) {
        super(ErrorFamily.UNIQUE_VIOLATION, errors);
    }
}
