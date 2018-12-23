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

package com.bellotapps.webapps_commons.data_transfer.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * An extension of {@link ObjectMapper} to be used within jersey, as a JSON Provider for the API to be built.
 */
public class ApiObjectMapper extends ObjectMapper {

    /**
     * Constructor.
     */
    public ApiObjectMapper() {
        // Serialization
        this.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        // Deserialization
        this.disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
        this.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        this.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }
}
