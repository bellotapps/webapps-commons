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

import org.springframework.util.Assert;

import java.util.Objects;

/**
 * Concrete implementation of {@link PagingRequest}.
 */
/* package */ class PagingRequestImpl implements PagingRequest {

    /**
     * The number of the {@link Page} or {@link Slice} that is being requested.
     */
    private final int pageNumber;
    /**
     * The size of the {@link Page} or {@link Slice} being requested.
     */
    private final int pageSize;
    /**
     * The {@link SortingData} to be passed to a repository.
     */
    private final SortingData sortBy;


    /**
     * Constructor.
     *
     * @param pageNumber The number of the {@link Page} or {@link Slice} that is being requested.
     * @param pageSize   The size of the {@link Page} or {@link Slice} being requested.
     * @param sortBy     The {@link SortingData} to be passed to a repository.
     */
    /* package */ PagingRequestImpl(final int pageNumber, final int pageSize, final SortingData sortBy) {
        Assert.isTrue(pageNumber >= 0, "The page index must not be negative");
        Assert.isTrue(pageSize > 0, "The page index must be positive");
        Assert.notNull(sortBy, "The sorting data must not be null.");
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
    }


    @Override
    public int pageNumber() {
        return pageNumber;
    }

    @Override
    public int pageSize() {
        return pageSize;
    }

    @Override
    public long offset() {
        return (long) pageNumber * (long) pageSize;
    }

    @Override
    public SortingData sortBy() {
        return sortBy;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PagingRequestImpl)) {
            return false;
        }
        final var that = (PagingRequestImpl) o;
        return pageNumber == that.pageNumber &&
                pageSize == that.pageSize &&
                sortBy.equals(that.sortBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageNumber, pageSize, sortBy);
    }

    @Override
    public String toString() {
        return String.format("Paging and sorting request [number: %d, size %d, sort %s]", pageNumber, pageSize, sortBy);
    }
}
