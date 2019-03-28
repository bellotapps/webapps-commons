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

package com.bellotapps.webapps_commons.persistence.spring_data;

import com.bellotapps.webapps_commons.exceptions.InvalidPropertiesException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Class in charge of validating if a {@link Pageable} can be used to query for entities of a given {@link Class}.
 */
public class PageableValidator {

    /**
     * Validates that the given {@link Pageable} is valid for querying entities of the given {@code klass}.
     *
     * @param pageable The {@link Pageable} to be validated.
     * @param klass    The class representing the entity.
     * @param <T>      The concrete type of the entities.
     * @throws InvalidPropertiesException If it has a {@link org.springframework.data.domain.Sort}
     *                                    with invalid properties.
     */
    public static <T> void validatePageable(final Pageable pageable, final Class<T> klass)
            throws InvalidPropertiesException {
        final var sort = pageable.getSort();
        // // First check if the pageable is sorted.
        if (sort.isUnsorted()) {
            return;
        }
        // If reached here, then there is sorting information in the pageable. Proceed with validation
        // Get fields of the entity (to check if the Sort contains any property that the entity does not have).
        final var entityFields = Arrays.stream(klass.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());
        // Check if there are properties in the Sort that the entity does not have.
        final var invalidProperties = sort.stream()
                .map(Sort.Order::getProperty)
                .filter(property -> !entityFields.contains(property))
                .collect(Collectors.toList());
        // Throw an InvalidPropertiesException if the Sort contains properties that the entity does not have.
        if (!invalidProperties.isEmpty()) {
            throw new InvalidPropertiesException(invalidProperties);
        }
    }
}
