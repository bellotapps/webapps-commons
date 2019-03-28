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

package com.bellotapps.webapps_commons.persistence.repository_utils.paging_and_sorting;

import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents an object that encapsulates information about sorting when performing queries to repositories.
 */
public class SortingData {

    /**
     * Holds an instance of an unsorted {@link SortingData} that can be reused.
     */
    private static final SortingData EMPTY_SORTING_DATA = new SortingData(Collections.emptyList());

    /**
     * List of {@link PropertySort}s that contain information about how the sorting should be done to each property.
     */
    private final List<PropertySort> sortBy;


    /**
     * Constructor.
     *
     * @param sortBy List of {@link PropertySort}s
     *               that contain information about how the sorting should be done to each property.
     */
    private SortingData(final List<PropertySort> sortBy) {
        Assert.notNull(sortBy, "The sortBy list must not be null.");
        this.sortBy = Collections.unmodifiableList(sortBy);
    }


    /**
     * @return An unsorted {@link SortingData} (i.e no sorting information).
     */
    public static SortingData unsorted() {
        return EMPTY_SORTING_DATA;
    }

    /**
     * Creates a {@link SortingData.Builder} which can be used to create a new {@link SortingData} instance.
     *
     * @return A new {@link SortingData.Builder}.
     */
    public static SortingData.Builder builder() {
        return new SortingData.Builder();
    }


    /**
     * @return The list of {@link PropertySort}s
     * that contain information about how the sorting should be done to each property.
     */
    public List<PropertySort> getSortBy() {
        return sortBy;
    }

    /**
     * Indicates whether there is sorting information.
     *
     * @return {@code true} if there is sorting information, or {@code false} otherwise.
     */
    public boolean isSorted() {
        return !isUnsorted();
    }

    /**
     * Indicates whether there is no sorting information.
     *
     * @return {@code true} if there is no sorting information, or {@code false} otherwise.
     */
    public boolean isUnsorted() {
        return sortBy.isEmpty();
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof SortingData)) {
            return false;
        }
        return sortBy.equals(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sortBy);
    }

    @Override
    public String toString() {
        if (isUnsorted()) {
            return "Unsorted";
        }
        return sortBy.stream().map(Objects::toString).collect(Collectors.joining("; "));
    }


    /**
     * Builder class for a {@link SortingData}.
     */
    public static final class Builder {

        /**
         * List of {@link PropertySort}s that contain information about how the sorting should be done to each property.
         */
        private final List<PropertySort> sortBy;


        /**
         * Constructor.
         */
        private Builder() {
            sortBy = new LinkedList<>();
        }


        /**
         * Creates a new {@link PropertySort.Builder} in order to configure a {@link PropertySort} that will be added
         * into this builder.
         *
         * @return A {@link PropertySort.Builder} that holds this builder.
         */
        public PropertySort.Builder sort() {
            return new PropertySort.Builder(this);
        }

        /**
         * Adds a {@link PropertySort} into this builder.
         *
         * @param propertySort The {@link PropertySort} to be added.
         * @return {@code this} (for method chaining).
         */
        public Builder sortBy(final PropertySort propertySort) {
            Assert.notNull(propertySort, "The propertySort must not be null.");
            this.sortBy.add(propertySort);
            return this;
        }

        /**
         * Builds a {@link SortingData} using the added {@link PropertySort}s.
         *
         * @return The created {@link SortingData}.
         */
        public SortingData build() {
            return new SortingData(sortBy);
        }
    }


    /**
     * Object that encapsulates information about how to sort a given property.
     */
    public static class PropertySort {

        /**
         * The property to which the sort will be applied.
         */
        private final String property;
        /**
         * The sorting direction (i.e asc or desc).
         */
        private final SortDirection direction;
        /**
         * A flag indicating whether the sorting should be case insensitive ({@code true} if yes, {@code false} if not).
         */
        private final boolean ignoreCase;
        /**
         * The strategy used for handling null values.
         */
        private final NullHandling nullHandling;


        /**
         * Constructor.
         *
         * @param property     The property to which the sort will be applied.
         * @param direction    The sorting direction (i.e asc or desc).
         * @param ignoreCase   A flag indicating whether the sorting should be case insensitive
         *                     ({@code true} if yes, {@code false} if not).
         * @param nullHandling The strategy used for handling null values.
         */
        private PropertySort(final String property,
                             final SortDirection direction,
                             final boolean ignoreCase,
                             final NullHandling nullHandling) {
            // Sanity check
            Assert.notNull(property, "The property must not be null");
            Assert.notNull(direction, "The direction must not be null");
            Assert.notNull(nullHandling, "The null handling strategy must not be null");
            this.property = property;
            this.direction = direction;
            this.ignoreCase = ignoreCase;
            this.nullHandling = nullHandling;
        }


        public static PropertySort.Builder builder() {
            return new PropertySort.Builder();
        }


        /**
         * @return The property to which the sort will be applied.
         */
        public String getProperty() {
            return property;
        }

        /**
         * @return The sorting direction (i.e asc or desc).
         */
        public SortDirection getDirection() {
            return direction;
        }

        /**
         * @return A flag indicating whether the sorting should be case insensitive
         * ({@code true} if yes, {@code false} if not).
         */
        public boolean isIgnoreCase() {
            return ignoreCase;
        }

        /**
         * @return The strategy used for handling null values.
         */
        public NullHandling getNullHandling() {
            return nullHandling;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof PropertySort)) {
                return false;
            }
            final var that = (PropertySort) o;
            return ignoreCase == that.ignoreCase &&
                    property.equals(that.property) &&
                    direction == that.direction &&
                    nullHandling == that.nullHandling;
        }

        @Override
        public int hashCode() {
            return Objects.hash(property, direction, ignoreCase, nullHandling);
        }

        @Override
        public String toString() {
            final var stringBuilder = new StringBuilder()
                    .append(property).append(", ").append(direction);
            if (!NullHandling.NATIVE.equals(nullHandling)) {
                stringBuilder.append(", ").append(nullHandling);
            }
            if (ignoreCase) {
                stringBuilder.append(", ignoring case");
            }
            return stringBuilder.toString();
        }

        /**
         * Builder class for a {@link PropertySort}.
         */
        public static class Builder {

            /**
             * Default sort direction.
             */
            private static final SortDirection DEFAULT_SORT_DIRECTION = SortDirection.defaultDirection();
            /**
             * Default ignore case flag.
             */
            private static final boolean DEFAULT_IGNORE_CASE = false;
            /**
             * Default null handling strategy.
             */
            private static final NullHandling DEFAULT_NULL_HANDLING = NullHandling.NATIVE;

            /**
             * Property placeholder.
             */
            private String property;
            /**
             * Direction placeholder.
             */
            private SortDirection direction;
            /**
             * Ignore case flag placeholder.
             */
            private boolean ignoreCase;
            /**
             * Null handling strategy placeholder.
             */
            private NullHandling nullHandling;

            /**
             * Holds the {@link SortingData.Builder} from which this builder was created.
             */
            private final SortingData.Builder comingFrom;


            /**
             * Constructor.
             */
            private Builder() {
                this.property = null;
                this.direction = DEFAULT_SORT_DIRECTION;
                this.ignoreCase = DEFAULT_IGNORE_CASE;
                this.nullHandling = DEFAULT_NULL_HANDLING;
                this.comingFrom = null;
            }

            /**
             * Constructor to be called with a {@link SortingData.Builder}.
             * This can be used for chaining the creation of a {@link SortingData} using its builder
             * together with this builder.
             *
             * @param comingFrom Holds the {@link SortingData.Builder} from which this builder is created.
             */
            private Builder(final SortingData.Builder comingFrom) {
                Assert.notNull(comingFrom, "The SortingData.Builder must not be null.");
                this.property = null;
                this.direction = DEFAULT_SORT_DIRECTION;
                this.ignoreCase = DEFAULT_IGNORE_CASE;
                this.nullHandling = DEFAULT_NULL_HANDLING;
                this.comingFrom = comingFrom;
            }


            /**
             * Configures the property to which sorting will be applied.
             *
             * @param property The property to which the sort will be applied.
             * @return {@code this} (for method chaining).
             */
            public Builder by(final String property) {
                Assert.notNull(property, "The property must not be null.");
                this.property = property;
                return this;
            }

            /**
             * Configures the sorting direction.
             *
             * @param direction The sorting direction (i.e asc or desc).
             * @return {@code this} (for method chaining).
             */
            public Builder withDirection(final SortDirection direction) {
                Assert.notNull(direction, "The direction must not be null.");
                this.direction = direction;
                return this;
            }

            /**
             * Configures the ignore case flag.
             *
             * @param ignoreCase A flag indicating whether the sorting should be case insensitive
             *                   ({@code true} if yes, {@code false} if not).
             * @return {@code this} (for method chaining).
             */
            public Builder withIgnoreCaseFlag(final boolean ignoreCase) {
                this.ignoreCase = ignoreCase;
                return this;
            }

            /**
             * Configures the null handling strategy.
             *
             * @param nullHandling The strategy used for handling null values.
             * @return {@code this} (for method chaining).
             */
            public Builder withNullHandling(final NullHandling nullHandling) {
                Assert.notNull(nullHandling, "The NullHandling strategy must not be null.");
                this.nullHandling = nullHandling;
                return this;
            }

            /**
             * Builds a {@link PropertySort} using set values.
             *
             * @return The created {@link PropertySort}.
             */
            public PropertySort build() {
                Assert.notNull(property, "The property has not been set.");
                return new PropertySort(property, direction, ignoreCase, nullHandling);
            }

            /**
             * Builds a {@link PropertySort} which is then added to the {@link SortingData.Builder}
             * from which this builder was built from.
             * Then returns that builder.
             *
             * @return The {@link SortingData.Builder} from which this builder was built from.
             * @see Builder#Builder(SortingData.Builder)
             */
            public SortingData.Builder and() {
                Assert.notNull(comingFrom, "Call this method if a SortingData.Builder was set.");
                this.comingFrom.sortBy(build());
                return comingFrom;
            }
        }
    }


    /**
     * Enum containing the sort directions (i.e ascending and descending).
     */
    public enum SortDirection {
        /**
         * The ascending direction.
         */
        ASC,
        /**
         * The descending direction.
         */
        DESC,
        ;

        /**
         * Returns whether the direction is ascending.
         *
         * @return {@code true} if this {@link SortDirection} corresponds to the ascending direction
         * (i.e {@code this.equals(SortDirection.ASC)}.
         */
        public boolean isAscending() {
            return this.equals(ASC);
        }

        /**
         * Returns whether the direction is descending.
         *
         * @return {@code true} if this {@link SortDirection} corresponds to the descending direction
         * (i.e {@code this.equals(SortDirection.DESC)}.
         */
        public boolean isDescending() {
            return this.equals(DESC);
        }

        /**
         * @return The default sort direction.
         */
        public static SortDirection defaultDirection() {
            return ASC;
        }

        /**
         * Returns the {@link SortDirection} that corresponds to the given {@code value}.
         *
         * @param value The sort direction value (i.e "asc" or "desc")
         * @return The {@link SortDirection} that corresponds to the given {@code value}.
         * @throws IllegalArgumentException in case the given value cannot be parsed into an enum value.
         */
        public static SortDirection fromString(final String value) {
            try {
                return SortDirection.valueOf(value.toUpperCase(Locale.US));
            } catch (final Exception e) {
                throw new IllegalArgumentException("Invalid sort direction. Must be \"asc\" or \"desc\"", e);
            }
        }

        /**
         * Returns the {@link SortDirection} that corresponds to the given {@code value}.
         *
         * @param value The sort direction value (i.e "asc" or "desc")
         * @return An {@link Optional} containing the {@link SortDirection} that corresponds to the given {@code value},
         * if there is such, or empty otherwise
         * (i.e the {@code value} coult not be parsed into a {@link SortDirection}).
         */
        public static Optional<SortDirection> fromStringOptional(final String value) {
            try {
                return Optional.of(fromString(value));
            } catch (final IllegalArgumentException e) {
                return Optional.empty();
            }
        }
    }


    /**
     * Enum containing the different types of null handling strategies (i.e nulls first or nulls last).
     * It also contains a value which indicates that the handling should be performed by the underlying infrastructure.
     */
    public enum NullHandling {
        /**
         * The underlying persistence infrastructure is in charge of handling nulls.
         */
        NATIVE,

        /**
         * Entries with null values before non null entries.
         */
        NULLS_FIRST,

        /**
         * Entries with null values after non null entries.
         */
        NULLS_LAST,
        ;
    }
}
