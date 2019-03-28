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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * A slice of data.
 *
 * @param <T> The type of content in this slice.
 */
public interface Slice<T> {

    // ================================================================================================================
    // Basic stuff
    // ================================================================================================================

    /**
     * @return Number of slice.
     */
    int number();

    /**
     * @return Slice size.
     */
    int size();

    /**
     * @return {@link SortingData} that was used to sort contents of this slice.
     */
    SortingData sortingData();

    /**
     * @return Amount of elements in the slice (can be less than the slice size if it is the last one).
     */
    int numberOfElements();

    /**
     * @return The offset to be taken according to the underlying {@link Slice} number and size.
     */
    long offset();

    /**
     * @return Content in this slice.
     */
    List<T> content();

    /**
     * Indicates whether there are elements in this slice.
     *
     * @return {@code true} if there are elements, or {@code false} otherwise.
     */
    boolean hasContent();

    /**
     * Maps the content of this slice according to the given {@code mapper}.
     *
     * @param mapper A {@link Function} that maps the contents of this slice.
     * @param <U>    Concrete type of the resulting contents.
     * @return A new slice containing the mapped elements.
     */
    <U> Slice<U> map(Function<T, U> mapper);

    /**
     * @return A {@link PagingRequest} from which this slice can be requested.
     */
    PagingRequest requestedBy();


    // ================================================================================================================
    // First
    // ================================================================================================================

    /**
     * Indicates whether this is the first slice.
     *
     * @return {@code true} if it is the first slice, or {@code false} otherwise.
     */
    boolean isFirst();

    /**
     * @return A {@link PagingRequest} from which the first slice can be requested.
     */
    PagingRequest firstRequest();


    // ================================================================================================================
    // Previous
    // ================================================================================================================

    /**
     * Indicates whether there is a previous slice.
     *
     * @return {@code true} if there is a previous slice, or {@code false} otherwise.
     */
    boolean hasPrevious();

    /**
     * @return An {@link Optional} of {@link PagingRequest}
     * from which the previous slice can be requested, if there is such, or empty otherwise.
     */
    Optional<PagingRequest> previousRequest();


    // ================================================================================================================
    // Next
    // ================================================================================================================

    /**
     * Indicates whether this is the last slice.
     *
     * @return {@code true} if it is the last slice, or {@code false} otherwise.
     */
    boolean hasNext();

    /**
     * @return An {@link Optional} of {@link PagingRequest}
     * from which the next slice can be requested, if there is such, or empty otherwise.
     */
    Optional<PagingRequest> nextRequest();


    // ================================================================================================================
    // Last
    // ================================================================================================================

    /**
     * Indicates whether this is the last slice.
     *
     * @return {@code true} if it is the last slice, or {@code false} otherwise.
     */
    boolean isLast();


    // ================================================================================================================
    // Factories
    // ================================================================================================================

    /**
     * Creates an empty {@link Slice}.
     *
     * @param <S> The type of elements in the slice.
     * @return An empty {@link Slice}.
     */
    static <S> Slice<S> empty() {
        return of(0, 0, SortingData.unsorted(), Collections.emptyList(), false);
    }

    /**
     * Creates an empty unsorted {@link Slice}, using the given paging information.
     *
     * @param number  The number of the slice.
     * @param size    The size of the slice.
     * @param hasNext A flag indicating whether there is another slice.
     * @param <S>     The type of elements in the slice.
     * @return An unsorted empty {@link Slice}.
     */
    static <S> Slice<S> emptyUnsorted(final int number, final int size, final boolean hasNext) {
        return of(number, size, SortingData.unsorted(), Collections.emptyList(), hasNext);
    }

    /**
     * Creates an empty {@link Slice}, using the given paging and sorting information.
     *
     * @param number      The number of the slice.
     * @param size        The size of the slice.
     * @param sortingData The {@link SortingData} that was used to sort contents of this slice.
     * @param hasNext     A flag indicating whether there is another slice.
     * @param <S>         The type of elements in the slice.
     * @return An empty {@link Slice}.
     */
    static <S> Slice<S> empty(final int number, final int size, final SortingData sortingData, final boolean hasNext) {
        return of(number, size, sortingData, Collections.emptyList(), hasNext);
    }

    /**
     * Creates an unsorted {@link Slice}.
     *
     * @param number  The number of the slice.
     * @param size    The size of the slice.
     * @param content The elements in the slice.
     * @param hasNext A flag indicating whether there is another slice.
     * @param <S>     The type of elements in the slice.
     * @return An unsorted {@link Slice}.
     */
    static <S> Slice<S> ofUnsorted(final int number, final int size, final List<S> content, final boolean hasNext) {
        return of(number, size, SortingData.unsorted(), content, hasNext);
    }

    /**
     * Creates a {@link Slice}.
     *
     * @param number      The number of the slice.
     * @param size        The size of the slice.
     * @param sortingData The {@link SortingData} that was used to sort contents of this slice.
     * @param content     The elements in the slice.
     * @param hasNext     A flag indicating whether there is another slice.
     * @param <S>         The type of elements in the slice.
     * @return A {@link Slice}.
     */
    static <S> Slice<S> of(final int number, final int size, final SortingData sortingData, final List<S> content,
                           final boolean hasNext) {
        return new SliceImpl<>(number, size, sortingData, content, hasNext);
    }
}
