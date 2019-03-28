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

/**
 * Data transfer object for transferring only one object of type {@code <T>} value with an API consumer.
 *
 * @param <T> The concrete type of object to be transferred.
 */
public abstract class GenericValueDto<T> {

    /**
     * The value to be transferred.
     */
    private final T value;

    /**
     * Constructor.
     *
     * @param value The value to be transferred.
     */
    protected GenericValueDto(final T value) {
        this.value = value;
    }

    /**
     * @return The value to be transferred.
     */
    public T getValue() {
        return value;
    }
}
