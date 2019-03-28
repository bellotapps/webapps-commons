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

package com.bellotapps.webapps_commons.data_transfer.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.net.URI;

/**
 * {@link com.fasterxml.jackson.databind.JsonSerializer} to transform a {@link URI} to a {@link String}.
 */
public class URISerializer extends StdSerializer<URI> {

    /**
     * Default constructor.
     */
    public URISerializer() {
        super(URI.class);
    }

    @Override
    public void serialize(final URI value, final JsonGenerator gen, final SerializerProvider provider)
            throws IOException {
        gen.writeString(value.toString());
    }
}
