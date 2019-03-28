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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * An abstract {@link Slice}, which implements common logic to all type of {@link Slice}s.
 */
public abstract class AbstractSlice<T> implements Slice<T> {

    /**
     * Number of slice.
     */
    private final int number;
    /**
     * Slice size.
     */
    private final int size;
    /**
     * The {@link SortingData} that was used to sort contents of this slice.
     */
    private final SortingData sortingData;
    /**
     * Content in this slice.
     */
    private final List<T> content;


    /**
     * Constructor.
     *
     * @param number      Number of slice.
     * @param size        Slice size.
     * @param sortingData The {@link SortingData} that was used to sort contents of this slice.
     * @param content     Content in this slice.
     */
    public AbstractSlice(final int number,
                         final int size,
                         final SortingData sortingData,
                         final List<T> content) {
        Assert.isTrue(number >= 0, "The index must not be negative");
        Assert.isTrue(size >= 0, "The size must not be negative");
        Assert.notNull(sortingData, "The sorting data must not be null");
        Assert.notNull(content, "The content list must not be null");
        this.number = number;
        this.size = size;
        this.sortingData = sortingData;
        this.content = Collections.unmodifiableList(content);
    }


    @Override
    public int number() {
        return number;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public SortingData sortingData() {
        return sortingData;
    }

    @Override
    public int numberOfElements() {
        return content.size();
    }

    @Override
    public long offset() {
        return (long) number * (long) size;
    }

    @Override
    public List<T> content() {
        return content;
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public PagingRequest requestedBy() {
        return PagingRequest.of(number, size, sortingData);
    }

    @Override
    public boolean isFirst() {
        return number == 0;
    }

    @Override
    public PagingRequest firstRequest() {
        return PagingRequest.of(0, size, sortingData);
    }

    @Override
    public boolean hasPrevious() {
        return number > 0;
    }

    @Override
    public Optional<PagingRequest> previousRequest() {
        if (number == 0) {
            return Optional.empty();
        }
        return Optional.of(PagingRequest.of(number - 1, size, sortingData));
    }

    @Override
    public Optional<PagingRequest> nextRequest() {
        if (hasNext()) {
            return Optional.of(PagingRequest.of(number + 1, size, sortingData));
        }
        return Optional.empty();
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }
}
