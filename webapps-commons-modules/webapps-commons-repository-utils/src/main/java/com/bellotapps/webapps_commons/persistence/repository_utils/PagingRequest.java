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

package com.bellotapps.webapps_commons.persistence.repository_utils;


/**
 * Defines behaviour for an object that contains information
 * that can be used to request a {@link Page} or a {@link Slice} to a repository, including sorting information.
 */
public interface PagingRequest {

    // ================================================================================================================
    // Basic stuff
    // ================================================================================================================

    /**
     * @return The number of the {@link Page} or {@link Slice} that is being requested.
     */
    int pageNumber();

    /**
     * @return The size of the {@link Page} or {@link Slice} being requested.
     */
    int pageSize();

    /**
     * @return The offset to be taken according to the underlying {@link Page} or {@link Slice} number and size.
     */
    long offset();

    /**
     * @return The {@link SortingData} to be passed to a repository.
     */
    SortingData sortBy();


    // ================================================================================================================
    // Factories
    // ================================================================================================================

    /**
     * Creates a new {@link PagingRequest}.
     *
     * @param pageNumber  The number of the {@link Page} or {@link Slice} that is being requested.
     * @param pageSize    The size of the {@link Page} or {@link Slice} being requested.
     * @param sortingData The {@link SortingData} to be passed to a repository when querying.
     * @return A {@link PagingRequest} with the specified paging and sorting data.
     */
    static PagingRequest of(final int pageNumber, final int pageSize, final SortingData sortingData) {
        return new PagingRequestImpl(pageNumber, pageSize, sortingData);
    }

    /**
     * Creates a new {@link PagingRequest} that contains no sorting data.
     *
     * @param pageNumber The number of the {@link Page} or {@link Slice} that is being requested.
     * @param pageSize   The size of the {@link Page} or {@link Slice} being requested.
     * @return A {@link PagingRequest} with no sorting data.
     */
    static PagingRequest unsorted(final int pageNumber, final int pageSize) {
        return of(pageNumber, pageSize, SortingData.unsorted());
    }
}
