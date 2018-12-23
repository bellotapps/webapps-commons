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

package com.bellotapps.webapps_commons.data_transfer.json.serializers;

import com.bellotapps.webapps_commons.data_transfer.date_time.DateTimeFormatters;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * {@link com.fasterxml.jackson.databind.JsonSerializer} to transform a {@link LocalDate} to a {@link String},
 * using {@link DateTimeFormatters#ISO_LOCAL_DATE} {@link DateTimeFormatter}.
 */
public class Java8ISOLocalDateSerializer extends StdSerializer<LocalDate> {

    /**
     * Default constructor.
     */
    public Java8ISOLocalDateSerializer() {
        super(LocalDate.class);
    }

    @Override
    public void serialize(final LocalDate value, final JsonGenerator gen, final SerializerProvider provider)
            throws IOException {
        gen.writeString(DateTimeFormatters.ISO_LOCAL_DATE.getFormatter().format(value));

    }
}
