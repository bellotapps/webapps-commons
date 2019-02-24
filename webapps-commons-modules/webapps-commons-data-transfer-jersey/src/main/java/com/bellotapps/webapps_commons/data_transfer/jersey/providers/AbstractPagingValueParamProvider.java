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
import org.glassfish.jersey.server.ParamException;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.ws.rs.QueryParam;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A {@link ValueParamProvider} to process paging information with the "page", "size", and "sort" query params.
 */

public abstract class AbstractPagingValueParamProvider<C, S, P, D> implements ValueParamProvider {

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
     * Class of the parameter whose value must be provided.
     */
    private final Class<C> parameterClass;

    /**
     * An {@link InjectionManager} to be used by the {@link PagingDataProvider}
     */
    private final InjectionManager injectionManager;
    /**
     * A {@link Predicate} of {@link Optional} of {@link String} to be used to validate sorting data
     * (property + direction).
     */
    private final Predicate<SortOrderData> sortOrderDataValidator;
    /**
     * A {@link BiFunction} that takes page number and size, and returns an unsorted paging data.
     */
    private final BiFunction<Integer, Integer, C> unsortedPagingProvider;
    /**
     * A {@link Function} that takes a {@link String} and returns an {@link Optional} of a direction object,
     * if the given {@link String} can be parsed into it.
     */
    private final Function<String, Optional<D>> directionProvider;
    /**
     * A {@link Supplier} that provides the default direction.
     */
    private final Supplier<D> defaultDirectionProvider;
    /**
     * A {@link BiFunction} that takes a property (represented as a {@link String}) and a direction
     * and returns an object that has information about how to sort by the given property.
     */
    private final BiFunction<String, D, P> propertySortProvider;
    /**
     * Function that indicates how to build the sorting information object
     * from a {@link List} of property sorting information objects.
     */
    private final Function<List<P>, S> finisher;
    /**
     * A paging data supplier (from the page number, size, and sorting information).
     */
    private final PagingDataSupplier<C, S> pagingDataProvider;


    /**
     * Constructor.
     *
     * @param parameterClass           Class of the parameter whose value must be provided.
     * @param injectionManager         An {@link InjectionManager} to be used by the {@link PagingDataProvider}
     * @param unsortedPagingProvider   A {@link BiFunction} that takes page number and size,
     *                                 and returns an unsorted paging data.
     * @param directionProvider        A {@link Function} that takes a {@link String}
     *                                 and returns an {@link Optional} of a direction object,
     *                                 if the given {@link String} can be parsed into it.
     * @param defaultDirectionProvider A {@link Supplier} that provides the default direction.
     * @param propertySortProvider     A {@link BiFunction} that takes a property
     *                                 (represented as a {@link String}) and a direction
     *                                 and returns an object that has information
     *                                 about how to sort by the given property.
     * @param finisher                 Function that indicates how to build the sorting information object
     *                                 from a {@link List} of property sorting information objects.
     * @param pagingDataProvider       A paging data supplier (from the page number, size, and sorting information).
     */
    protected AbstractPagingValueParamProvider(
            final Class<C> parameterClass,
            final InjectionManager injectionManager,
            final BiFunction<Integer, Integer, C> unsortedPagingProvider,
            final Function<String, Optional<D>> directionProvider,
            final Supplier<D> defaultDirectionProvider,
            final BiFunction<String, D, P> propertySortProvider,
            final Function<List<P>, S> finisher,
            final PagingDataSupplier<C, S> pagingDataProvider) {
        this.parameterClass = parameterClass;
        this.injectionManager = injectionManager;
        this.unsortedPagingProvider = unsortedPagingProvider;
        this.directionProvider = directionProvider;
        this.defaultDirectionProvider = defaultDirectionProvider;
        this.propertySortProvider = propertySortProvider;
        this.finisher = finisher;
        this.pagingDataProvider = pagingDataProvider;

        final Predicate<Optional<String>> propertyValidator = optional ->
                optional.filter(StringUtils::hasText).isPresent();
        final Predicate<Optional<String>> directionValidator = optional ->
                optional.isEmpty() || optional.flatMap(directionProvider).isPresent();
        this.sortOrderDataValidator = data ->
                propertyValidator.test(data.getProperty()) && directionValidator.test(data.getDirection());
    }


    @Override
    public Function<ContainerRequest, ?> getValueProvider(final Parameter parameter) {
        // This ValueFactoryProvider only supports the Pageable interface (or any class implementing it) parameters
        // that are annotated with PaginationParam
        if (ClassUtils.isAssignable(parameterClass, parameter.getRawType())
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

            // Create the PagingDataProvider
            return new PagingDataProvider<>(injectionManager, sortOrderDataValidator,
                    unsortedPagingProvider, directionProvider, defaultDirectionProvider,
                    propertySortProvider, finisher, pagingDataProvider, annotation);
        }
        return null;
    }

    @Override
    public PriorityType getPriority() {
        return Priority.NORMAL;
    }


    /**
     * A {@link Function} that takes a {@link ContainerRequest} and builds an object with paging information
     * to be used by the {@link AbstractPagingValueParamProvider}.
     *
     * @param <C> The type of object that contains paging information.
     * @param <S> The type of the object that holds sorting information.
     * @param <P> The type of the object that indicate how to sort a property.
     * @param <D> The type of object that describes sorting direction.
     */
    private static class PagingDataProvider<C, S, P, D> implements Function<ContainerRequest, C> {
        /**
         * The {@link InjectionManager} used to inject values from the context
         * when initializing {@link PaginationParams} instances.
         */
        private final InjectionManager injectionManager;
        /**
         * A {@link Predicate} of {@link Optional} of {@link String} to be used to validate sorting data
         * (property + direction).
         */
        private final Predicate<SortOrderData> sortOrderDataValidator;
        /**
         * A {@link BiFunction} that takes page number and size, and returns an unsorted paging data.
         */
        private final BiFunction<Integer, Integer, C> unsortedPagingProvider;
        /**
         * A {@link Function} that takes a {@link String} and returns an {@link Optional} of a direction object,
         * if the given {@link String} can be parsed into it.
         */
        private final Function<String, Optional<D>> directionProvider;
        /**
         * A {@link Supplier} that provides the default direction.
         */
        private final Supplier<D> defaultDirectionProvider;

        /**
         * A {@link BiFunction} that takes a property (represented as a {@link String}) and a direction
         * and returns an object that has information about how to sort by the given property.
         */
        private final BiFunction<String, D, P> propertySortProvider;
        /**
         * Function that indicates how to build the sorting information object
         * from a {@link List} of property sorting information objects.
         */
        private final Function<List<P>, S> finisher;
        /**
         * A paging data supplier (from the page number, size, and sorting information).
         */
        private final PagingDataSupplier<C, S> pagingDataProvider;

        /**
         * The {@link PaginationParam} instance from where pagination metadata can be obtained.
         */
        private final PaginationParam paginationParam;


        /**
         * @param injectionManager         An {@link InjectionManager} to be used by the {@link PagingDataProvider}
         * @param sortOrderDataValidator   A {@link Predicate} of {@link Optional} of {@link String}
         *                                 to be used to validate sorting data (property + direction).
         * @param unsortedPagingProvider   A {@link BiFunction} that takes page number and size,
         *                                 and returns an unsorted paging data.
         * @param directionProvider        A {@link Function} that takes a {@link String}
         *                                 and returns an {@link Optional} of a direction object,
         *                                 if the given {@link String} can be parsed into it.
         * @param defaultDirectionProvider A {@link Supplier} that provides the default direction.
         * @param propertySortProvider     A {@link BiFunction} that takes a property
         *                                 (represented as a {@link String}) and a direction
         *                                 and returns an object that has information
         *                                 about how to sort by the given property.
         * @param finisher                 Function that indicates how to build the sorting information object
         *                                 from a {@link List} of property sorting information objects.
         * @param pagingDataProvider       A paging data supplier (from the page number, size, and sorting information).
         * @param paginationParam          The {@link PaginationParam} instance
         *                                 from where pagination metadata can be obtained.
         */
        private PagingDataProvider(
                final InjectionManager injectionManager,
                final Predicate<SortOrderData> sortOrderDataValidator,
                final BiFunction<Integer, Integer, C> unsortedPagingProvider,
                final Function<String, Optional<D>> directionProvider,
                final Supplier<D> defaultDirectionProvider,
                final BiFunction<String, D, P> propertySortProvider,
                final Function<List<P>, S> finisher,
                final PagingDataSupplier<C, S> pagingDataProvider,
                final PaginationParam paginationParam) {
            this.injectionManager = injectionManager;
            this.sortOrderDataValidator = sortOrderDataValidator;
            this.propertySortProvider = propertySortProvider;
            this.directionProvider = directionProvider;
            this.defaultDirectionProvider = defaultDirectionProvider;
            this.pagingDataProvider = pagingDataProvider;
            this.paginationParam = paginationParam;
            this.finisher = finisher;
            this.unsortedPagingProvider = unsortedPagingProvider;
        }


        @Override
        public C apply(final ContainerRequest containerRequest) {
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
            if (sorts.stream().anyMatch(sortOrderDataValidator.negate())) {
                illegalValueParams.add(SORT_PARAM);
            }
            // Throw an IllegalParamValueException if any of the validations failed.
            if (!illegalValueParams.isEmpty()) {
                throw new IllegalParamValueException(illegalValueParams);
            }

            final var pageNumber = pageNumberOptional.orElse(paginationParam.defaultPageNumber());
            final var pageSize = pageSizeOptional.orElse(paginationParam.defaultPageSize());

            // If there is no sorting data, then return an unsorted paging information object.
            if (sorts.isEmpty()) {
                return unsortedPagingProvider.apply(pageNumber, pageSize);
            }
            // If reached here, sorting is required
            // Build the Sorting information object instance
            final var sort = sorts.stream()
                    .map(sortOrderData -> {
                        final var property = sortOrderData.getProperty()
                                .orElseThrow(() -> new RuntimeException("This should not happen"));
                        final var direction = sortOrderData.getDirection()
                                .flatMap(directionProvider)
                                .orElse(defaultDirectionProvider.get());
                        return propertySortProvider.apply(property, direction);
                    })
                    .collect(Collectors.collectingAndThen(Collectors.toList(), finisher));
            // Create the paging information object.
            return pagingDataProvider.provide(pageNumber, pageSize, sort);
        }
    }

    /**
     * Defines behaviour for a function that can create a paging data object from a page number, size and sorting data.
     *
     * @param <C> The type of paging data object.
     * @param <S> The type of sorting data object.
     */
    @FunctionalInterface
    protected interface PagingDataSupplier<C, S> {

        /**
         * Provides an paging object instance according to the given elements.
         *
         * @param number      The page number.
         * @param size        The page size.
         * @param sortingData The object with sorting information.
         * @return A paging object.
         */
        C provide(final int number, final int size, final S sortingData);
    }


    /**
     * A bean class that encapsulates elements to create a paging information object.
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
            try {
                this.sortOrderDataList = sortOrderDataList.stream()
                        .map(SortOrderData::fromString)
                        .collect(Collectors.toList());
            } catch (final Throwable e) {
                throw new ParamException.QueryParamException(e, SORT_PARAM, "");
            }
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
     * A bean class that encapsulates elements to create an object that indicates how to sort a property.
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
                throw new IllegalArgumentException("Invalid sorting data format");
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
