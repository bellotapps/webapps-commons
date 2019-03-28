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

package com.bellotapps.webapps_commons.persistence.spring_data.repository_utils_adapters.paging_and_sorting;

import com.bellotapps.webapps_commons.persistence.repository_utils.paging_and_sorting.SortingData;
import org.springframework.data.domain.Sort;

import java.util.stream.Collectors;

/**
 * A mapper for sorting information.
 */
public class SortingMapper {

    /**
     * Maps a {@link SortingData} into a {@link Sort}.
     *
     * @param sortingData The {@link SortingData} to be mapped into a {@link Sort}.
     * @return A {@link Sort} built from the information of the given {@code sortingData}.
     */
    public static Sort map(final SortingData sortingData) {
        if (sortingData.isUnsorted()) {
            return Sort.unsorted();
        }
        return sortingData.getSortBy().stream()
                .map(PropertySortAdapter::map)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Sort::by));
    }

    /**
     * Maps a {@link Sort} into a {@link SortingData}.
     *
     * @param sort The {@link Sort} to be mapped into a {@link SortingData}.
     * @return A {@link SortingData} built from the information of the given {@code sort}.
     */
    public static SortingData map(final Sort sort) {
        if (sort.isUnsorted()) {
            return SortingData.unsorted();
        }
        final var builder = SortingData.builder();
        for (final var order : sort) {
            builder.sortBy(PropertySortAdapter.map(order));
        }
        return builder.build();
    }

    /**
     * A mapper for objects with information about how to sort a property.
     */
    public static final class PropertySortAdapter {

        /**
         * Maps a {@link SortingData.PropertySort} into a {@link Sort.Order}.
         *
         * @param propertySort The {@link SortingData.PropertySort} to be mapped into a {@link Sort.Order}.
         * @return A {@link Sort.Order} built from the information of the given {@code propertySort}.
         */
        public static Sort.Order map(final SortingData.PropertySort propertySort) {
            final var order = Sort.Order
                    .by(propertySort.getProperty())
                    .with(DirectionAdapter.map(propertySort.getDirection()))
                    .with(NullHandlingAdapter.map(propertySort.getNullHandling()));
            return propertySort.isIgnoreCase() ? order.ignoreCase() : order;
        }

        /**
         * Maps a {@link Sort.Order} into a {@link SortingData.PropertySort}.
         *
         * @param order The {@link Sort.Order} to be mapped into a {@link SortingData.PropertySort}.
         * @return A {@link SortingData.PropertySort} built from the information of the given {@code order}.
         */
        public static SortingData.PropertySort map(final Sort.Order order) {
            return SortingData.PropertySort.builder()
                    .by(order.getProperty())
                    .withIgnoreCaseFlag(order.isIgnoreCase())
                    .withDirection(DirectionAdapter.map(order.getDirection()))
                    .withNullHandling(NullHandlingAdapter.map(order.getNullHandling()))
                    .build();
        }
    }

    /**
     * A mapper for objects that represent sorting direction.
     */
    public static final class DirectionAdapter {

        /**
         * Maps a {@link SortingData.SortDirection} into a {@link Sort.Direction}.
         *
         * @param sortDirection The {@link SortingData.SortDirection} to be mapped into a {@link Sort.Direction}.
         * @return A {@link Sort.Direction} that represents the same information of the given {@code sortDirection}.
         */
        public static Sort.Direction map(final SortingData.SortDirection sortDirection) {
            switch (sortDirection) {
                case ASC:
                    return Sort.Direction.ASC;
                case DESC:
                    return Sort.Direction.DESC;
                default:
                    throw new RuntimeException("This should not happen");
            }
        }

        /**
         * Maps a {@link Sort.Direction} into a {@link SortingData.SortDirection}.
         *
         * @param direction The {@link SortingData.SortDirection} to be mapped into a {@link Sort.Direction}.
         * @return A {@link SortingData.SortDirection}
         * that represents the same information of the given {@code direction}.
         */
        public static SortingData.SortDirection map(final Sort.Direction direction) {
            switch (direction) {
                case ASC:
                    return SortingData.SortDirection.ASC;
                case DESC:
                    return SortingData.SortDirection.DESC;
                default:
                    throw new RuntimeException("This should not happen");
            }
        }
    }

    /**
     * A mapper for objects that represent null handling strategies.
     */
    public static final class NullHandlingAdapter {

        /**
         * Maps a {@link SortingData.NullHandling} into a {@link Sort.NullHandling}.
         *
         * @param nullHandling The {@link SortingData.NullHandling} to be mapped into a {@link Sort.NullHandling}.
         * @return A {@link Sort.NullHandling} that represents the same information of the given {@code nullHandling}.
         */
        public static Sort.NullHandling map(final SortingData.NullHandling nullHandling) {
            switch (nullHandling) {
                case NATIVE:
                    return Sort.NullHandling.NATIVE;
                case NULLS_FIRST:
                    return Sort.NullHandling.NULLS_FIRST;
                case NULLS_LAST:
                    return Sort.NullHandling.NULLS_LAST;
                default:
                    throw new RuntimeException("This should not happen");
            }
        }

        /**
         * Maps a {@link Sort.NullHandling} into a {@link SortingData.NullHandling}.
         *
         * @param nullHandling The {@link SortingData.NullHandling} to be mapped into a {@link Sort.NullHandling}.
         * @return A {@link SortingData.NullHandling}
         * that represents the same information of the given {@code nullHandling}.
         */
        public static SortingData.NullHandling map(final Sort.NullHandling nullHandling) {
            switch (nullHandling) {
                case NATIVE:
                    return SortingData.NullHandling.NATIVE;
                case NULLS_FIRST:
                    return SortingData.NullHandling.NULLS_FIRST;
                case NULLS_LAST:
                    return SortingData.NullHandling.NULLS_LAST;
                default:
                    throw new RuntimeException("This should not happen");
            }
        }
    }
}
