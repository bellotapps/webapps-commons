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

package com.bellotapps.webapps_commons.persistence.repository_utils.paging_and_sorting;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * An extension of a {@link Slice} in which is known the overall amount of data.
 */
public interface Page<T> extends Slice<T> {

    // ================================================================================================================
    // Basic stuff
    // ================================================================================================================

    /**
     * @return The total amount of pages.
     */
    int totalPages();

    /**
     * @return The overall amount of elements.
     */
    long totalElements();

    /**
     * Maps the content of this page according to the given {@code mapper}.
     *
     * @param mapper A {@link Function} that maps the contents of this page.
     * @param <U>    Concrete type of the resulting contents.
     * @return A new page containing the mapped elements.
     */
    <U> Page<U> map(Function<T, U> mapper);


    // ================================================================================================================
    // Factories
    // ================================================================================================================

    /**
     * Creates an empty {@link Page}.
     *
     * @param <S> The type of elements in the page.
     * @return An empty {@link Page}.
     */
    static <S> Page<S> empty() {
        return of(0, 0, SortingData.unsorted(), Collections.emptyList(), 0);
    }

    /**
     * Creates an empty unsorted {@link Page}, using the given paging information.
     *
     * @param number        The number of the page.
     * @param size          The size of the page.
     * @param totalElements The overall amount of elements.
     * @param <S>           The type of elements in the page.
     * @return An unsorted empty {@link Page}.
     */
    static <S> Page<S> emptyUnsorted(final int number, final int size, final long totalElements) {
        return of(number, size, SortingData.unsorted(), Collections.emptyList(), totalElements);
    }

    /**
     * Creates an empty {@link Page}, using the given paging and sorting information.
     *
     * @param number        The number of the page.
     * @param size          The size of the page.
     * @param sortingData   The {@link SortingData} that was used to sort contents of this page.
     * @param totalElements The overall amount of elements.
     * @param <S>           The type of elements in the page.
     * @return An empty {@link Page}.
     */
    static <S> Page<S> empty(final int number, final int size, final SortingData sortingData,
                             final long totalElements) {
        return of(number, size, sortingData, Collections.emptyList(), totalElements);
    }

    /**
     * Creates an unsorted {@link Page}.
     *
     * @param number        The number of the page.
     * @param size          The size of the page.
     * @param content       The elements in the page.
     * @param totalElements The overall amount of elements.
     * @param <S>           The type of elements in the page.
     * @return An unsorted {@link Page}.
     */
    static <S> Page<S> ofUnsorted(final int number, final int size, final List<S> content, final long totalElements) {
        return of(number, size, SortingData.unsorted(), content, totalElements);
    }

    /**
     * Creates a {@link Page}.
     *
     * @param number        The number of the page.
     * @param size          The size of the page.
     * @param sortingData   The {@link SortingData} that was used to sort contents of this page.
     * @param content       The elements in the page.
     * @param totalElements The overall amount of elements.
     * @param <S>           The type of elements in the page.
     * @return A {@link Page}.
     */
    static <S> Page<S> of(final int number, final int size, final SortingData sortingData, final List<S> content,
                          final long totalElements) {
        return new PageImpl<>(number, size, sortingData, content, totalElements);
    }
}
