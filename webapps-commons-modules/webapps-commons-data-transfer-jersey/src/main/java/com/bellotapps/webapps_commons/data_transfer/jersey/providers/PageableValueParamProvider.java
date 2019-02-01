/*
 * Copyright 2019 BellotApps
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

package com.bellotapps.webapps_commons.data_transfer.jersey.providers;


import com.bellotapps.webapps_commons.data_transfer.jersey.annotations.PaginationParam;
import com.bellotapps.webapps_commons.exceptions.IllegalParamValueException;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ext.Provider;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * A {@link ValueParamProvider} to process {@link Pageable}s with the "page", "size", and "sort" query params.
 */
@Provider
public class PageableValueParamProvider implements ValueParamProvider {

    /**
     * Param name for the page number.
     */
    private final static String PAGE_NUMBER_PARAM = "page";
    /**
     * Param name for the page size.
     */
    private final static String PAGE_SIZE_PARAM = "size";
    /**
     * Param name for the sorting information (property,direction).
     */
    private final static String SORT_PARAM = "sort";

    /**
     * A {@link Predicate} of {@link Optional} of {@link String} to be used to validate a sorting property.
     */
    private final static Predicate<Optional<String>> PROPERTY_VALIDATOR =
            optional -> optional.filter(StringUtils::hasText).isPresent();

    /**
     * A {@link Predicate} of {@link Optional} of {@link String} to be used to validate a sorting direction.
     */
    private final static Predicate<Optional<String>> DIRECTION_VALIDATOR =
            optional -> optional.isEmpty() || optional.flatMap(Sort.Direction::fromOptionalString).isPresent();

    /**
     * A {@link Predicate} of {@link Optional} of {@link String} to be used to validate sorting data
     * (property + direction).
     */
    private final static Predicate<SortOrderData> SORT_ORDER_DATA_VALIDATOR =
            sortOrderData ->
                    PROPERTY_VALIDATOR.test(sortOrderData.getProperty())
                            && DIRECTION_VALIDATOR.test(sortOrderData.getDirection());

    /**
     * An {@link InjectionManager} to be used by the {@link PageableProvider}
     */
    private final InjectionManager injectionManager;


    /**
     * Constructor.
     *
     * @param injectionManager An {@link InjectionManager} to be used by the {@link PageableProvider}
     */
    @Inject
    public PageableValueParamProvider(final InjectionManager injectionManager) {
        this.injectionManager = injectionManager;
    }


    @Override
    public Function<ContainerRequest, ?> getValueProvider(final Parameter parameter) {
        // This ValueFactoryProvider only supports the Pageable interface (or any class implementing it) parameters
        // that are annotated with PaginationParam
        if (ClassUtils.isAssignable(Pageable.class, parameter.getRawType())
                && parameter.isAnnotationPresent(PaginationParam.class)) {
            // First, get the PaginationParam annotation from the parameter
            final var annotation = parameter.getAnnotation(PaginationParam.class);

            // Validate annotation values
            Assert.isTrue(annotation.defaultPageNumber() >= 0,
                    "The default page number must be positive or zero");
            Assert.isTrue(annotation.defaultPageSize() >= 1,
                    "The default page size must be positive");
            Assert.isTrue(annotation.defaultPageSize() <= annotation.maxPageSize(),
                    "The default page size must be lower than or equal to the max. page size");

            // Create the PageableProvider
            return new PageableProvider(injectionManager, annotation);
        }
        return null;
    }

    @Override
    public PriorityType getPriority() {
        return Priority.NORMAL;
    }


    /**
     * A {@link Function} that takes a {@link ContainerRequest} and builds a {@link Pageable},
     * to be used by the {@link PageableValueParamProvider}.
     */
    private static class PageableProvider implements Function<ContainerRequest, Pageable> {
        /**
         * The {@link InjectionManager} used to inject values from the context
         * when initializing {@link PaginationParams} instances.
         */
        private final InjectionManager injectionManager;
        /**
         * The {@link PaginationParam} instance from where pagination metadata can be obtained.
         */
        private final PaginationParam paginationParam;

        /**
         * Constructor.
         *
         * @param injectionManager The {@link InjectionManager} used to inject values from the context
         *                         when initializing {@link PaginationParams} instances.
         * @param paginationParam  The {@link PaginationParam} instance from where pagination metadata can be obtained.
         */
        private PageableProvider(
                final InjectionManager injectionManager,
                final PaginationParam paginationParam) {
            this.injectionManager = injectionManager;
            this.paginationParam = paginationParam;
        }

        @Override
        public Pageable apply(final ContainerRequest containerRequest) {
            final var paginationParams = injectionManager.createAndInitialize(PaginationParams.class);

            final var pageNumberOptional = paginationParams.getPageNumber();
            final var pageSizeOptional = paginationParams.getPageSize();
            final var sorts = paginationParams.getSortOrderDataList();

            final List<String> illegalValueParams = new LinkedList<>();
            // Validate page number
            pageNumberOptional.ifPresent(pageNumber -> {
                if (pageNumber < 0) {
                    illegalValueParams.add(PAGE_NUMBER_PARAM);
                }
            });
            // Validate page size
            pageSizeOptional.ifPresent(pageSize -> {
                if (pageSize < 1 || pageSize > paginationParam.maxPageSize()) {
                    illegalValueParams.add(PAGE_SIZE_PARAM);
                }
            });
            // Validate sort data
            if (sorts.stream().anyMatch(SORT_ORDER_DATA_VALIDATOR.negate())) {
                illegalValueParams.add(SORT_PARAM);
            }
            // Throw an IllegalParamValueException if any of the validations failed.
            if (!illegalValueParams.isEmpty()) {
                throw new IllegalParamValueException(illegalValueParams);
            }

            // Build the Sort instance
            final var sort = sorts.stream()
                    .map(sortOrderData -> {
                        final var order = sortOrderData.getProperty()
                                .map(Sort.Order::by)
                                .orElseThrow(() -> new RuntimeException("This should not happen"));
                        sortOrderData.getDirection()
                                .flatMap(Sort.Direction::fromOptionalString)
                                .ifPresent(order::with);
                        return order;
                    })
                    .collect(new SortOrderCollector());

            final var pageNumber = pageNumberOptional.orElse(paginationParam.defaultPageNumber());
            final var pageSize = pageSizeOptional.orElse(paginationParam.defaultPageSize());
            return PageRequest.of(pageNumber, pageSize, sort);
        }
    }

    /**
     * A collector that takes {@link org.springframework.data.domain.Sort.Order} instances,
     * and build a {@link Sort} from them.
     */
    private final static class SortOrderCollector implements Collector<Sort.Order, List<Sort.Order>, Sort> {

        @Override
        public Supplier<List<Sort.Order>> supplier() {
            return LinkedList::new;
        }

        @Override
        public BiConsumer<List<Sort.Order>, Sort.Order> accumulator() {
            return List::add;
        }

        @Override
        public BinaryOperator<List<Sort.Order>> combiner() {
            return (l1, l2) -> {
                l1.addAll(l2);
                return l1;
            };
        }

        @Override
        public Function<List<Sort.Order>, Sort> finisher() {
            return Sort::by;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of(Characteristics.CONCURRENT);
        }
    }


    /**
     * A bean class that encapsulates elements to create a {@link Pageable}.
     */
    private static final class PaginationParams {
        /**
         * The page number.
         */
        private final Integer pageNumber;
        /**
         * The page size.
         */
        private final Integer pageSize;
        /**
         * The sorting information.
         */
        private final List<SortOrderData> sortOrderDataList;

        /**
         * Constructor.
         *
         * @param pageNumber        The page number.
         * @param pageSize          The page size.
         * @param sortOrderDataList The sorting information (in {@link String} format).
         */
        private PaginationParams(@QueryParam(PAGE_NUMBER_PARAM) final Integer pageNumber,
                                 @QueryParam(PAGE_SIZE_PARAM) final Integer pageSize,
                                 @QueryParam(SORT_PARAM) final List<String> sortOrderDataList) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.sortOrderDataList = sortOrderDataList.stream()
                    .map(SortOrderData::fromString)
                    .collect(Collectors.toList());
        }

        /**
         * Wraps the page number in an {@link Optional}, as it might not be present.
         *
         * @return An {@link Optional} that might contain the page number.
         */
        private Optional<Integer> getPageNumber() {
            return Optional.ofNullable(pageNumber);
        }

        /**
         * Wraps the page size in an {@link Optional}, as it might not be present.
         *
         * @return An {@link Optional} that might contain the page size.
         */
        private Optional<Integer> getPageSize() {
            return Optional.ofNullable(pageSize);
        }

        /**
         * Wraps the sorting information in an {@link Optional}, as it might not be present.
         *
         * @return An {@link Optional} that might contain the sorting information.
         */
        private List<SortOrderData> getSortOrderDataList() {
            return Optional.ofNullable(sortOrderDataList).orElse(Collections.emptyList());
        }
    }

    /**
     * A bean class that encapsulates elements to create a {@link org.springframework.data.domain.Sort.Order}.
     */
    private static class SortOrderData {

        /**
         * The property used to sort.
         */
        private final String property;

        /**
         * The sorting direction.
         */
        private final String direction;

        /**
         * Constructor.
         *
         * @param property  The property used to sort.
         * @param direction The sorting direction.
         */
        private SortOrderData(final String property, final String direction) {
            this.property = property;
            this.direction = direction;
        }

        /**
         * @return The property used to sort.
         */
        private Optional<String> getProperty() {
            return Optional.ofNullable(property);
        }

        /**
         * @return The sorting direction.
         */
        private Optional<String> getDirection() {
            return Optional.ofNullable(direction);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SortOrderData)) {
                return false;
            }
            final var that = (SortOrderData) o;
            return Objects.equals(property, that.property) && Objects.equals(direction, that.direction);
        }

        @Override
        public int hashCode() {
            return Objects.hash(property, direction);
        }

        /**
         * Builds a {@link SortOrderData} from the given {@code stringValue}.
         *
         * @param stringValue The {@link String} holding the data from where the {@link SortOrderData} can be built.
         *                    Must have the following format: &lg;property&gt;[,&lg;direction&gt;].
         * @return The created {@link SortOrderData}.
         * @apiNote The given {@link String} must follow the following format: &lg;property&gt;[,&lg;direction&gt;].
         */
        private static SortOrderData fromString(final String stringValue) {
            // First, split the param using a ',' as delimiter.
            final var splitted = stringValue.split(",", -1);
            // Then, check only one or two elements exists in the array.
            if (splitted.length == 0 || splitted.length > 2) {
                throw new RuntimeException("invalid length");  // TODO: check if another exception is better
            }

            // Up to here we know that the format is correct.
            // We can proceed to build the SortOrderData, according to the presence or absence of the direction.
            final var property = splitted[0]; // The property is always present.
            if (splitted.length == 1) {
                // In this case, only the property is present.
                return new SortOrderData(property, null);
            }
            // If reached here, the direction is also present.
            final var direction = splitted[1];
            return new SortOrderData(property, direction);
        }
    }
}
