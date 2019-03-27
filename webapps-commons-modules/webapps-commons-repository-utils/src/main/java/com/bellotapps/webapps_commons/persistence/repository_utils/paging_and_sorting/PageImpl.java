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

import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Concrete implementation of {@link Page}.
 */
public class PageImpl<T> extends AbstractSlice<T> implements Page<T> {

    /**
     * The overall amount of elements.
     */
    private final long totalElements;


    /**
     * Constructor.
     *
     * @param number        Number of slice.
     * @param size          Slice size.
     * @param sortingData   The {@link SortingData} that was used to sort contents of this slice.
     * @param content       Content in this slice.
     * @param totalElements The overall amount of elements.
     */
    public PageImpl(final int number, final int size, final SortingData sortingData, final List<T> content,
                    final long totalElements) {
        super(number, size, sortingData, content);
        Assert.isTrue(totalElements >= 0, "The total amount of elements must not be negative");
        final var offset = offset();
        this.totalElements = content.isEmpty() || offset + size <= totalElements ?
                totalElements :
                offset + content.size();
    }


    @Override
    public int totalPages() {
        final var size = size();
        if (size == 0) {
            return 1;
        }
        return (int) Math.ceil((double) totalElements / (double) size);
    }

    @Override
    public long totalElements() {
        return totalElements;
    }

    @Override
    public boolean hasNext() {
        return number() + 1 < totalPages();
    }

    @Override
    public <U> Page<U> map(final Function<T, U> mapper) {
        final var mapped = content().stream().map(mapper).collect(Collectors.toList());
        return new PageImpl<>(number(), size(), sortingData(), mapped, totalElements);
    }
}
