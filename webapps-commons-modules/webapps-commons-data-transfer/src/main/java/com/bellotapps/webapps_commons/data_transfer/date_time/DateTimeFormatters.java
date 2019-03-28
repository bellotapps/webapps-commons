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

package com.bellotapps.webapps_commons.data_transfer.date_time;

import java.time.format.DateTimeFormatter;

/**
 * Enum containing {@link DateTimeFormatter}s to be reused.
 */
public enum DateTimeFormatters {

    ISO_LOCAL_DATE(DateTimeFormatter.ISO_LOCAL_DATE),
    ISO_LOCAL_TIME(DateTimeFormatter.ISO_LOCAL_TIME),
    ISO_LOCAL_DATE_TIME(DateTimeFormatter.ISO_LOCAL_DATE_TIME);


    /**
     * Holds the {@link DateTimeFormatter} for the enum value.
     */
    private final DateTimeFormatter formatter;

    /**
     * Constructor.
     *
     * @param pattern The pattern to build the {@link DateTimeFormatter}.
     */
    DateTimeFormatters(final String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    /**
     * Constructor.
     *
     * @param formatter The {@link DateTimeFormatter}.
     */
    DateTimeFormatters(final DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * @return The {@link DateTimeFormatter} for the enum value.
     */
    public DateTimeFormatter getFormatter() {
        return formatter;
    }
}
