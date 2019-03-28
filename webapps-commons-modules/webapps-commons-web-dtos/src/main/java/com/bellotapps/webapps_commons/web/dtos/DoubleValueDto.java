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

package com.bellotapps.webapps_commons.web.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data transfer object for transferring only one {@link Double} value with an API consumer.
 */
public class DoubleValueDto extends GenericValueDto<Double> {

    /**
     * @param value The value to be transferred.
     */
    @JsonCreator
    public DoubleValueDto(
            @JsonProperty(value = "value", access = JsonProperty.Access.WRITE_ONLY) final Double value) {
        super(value);
    }

    /**
     * @return The value to be transferred.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Double getValue() {
        return super.getValue();
    }
}
