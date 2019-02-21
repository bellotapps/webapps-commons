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


import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A concrete implementation of a {@link Slice}.
 */
public class SliceImpl<T> extends AbstractSlice<T> {

    /**
     * A flag indicating whether there is another slice.
     */
    private final boolean hasNext;


    /**
     * Constructor.
     *
     * @param number      Number of slice.
     * @param size        Slice size.
     * @param sortingData The {@link SortingData} that was used to sort contents of this slice.
     * @param content     Content in this slice.
     * @param hasNext     A flag indicating whether there is another slice.
     */
    public SliceImpl(final int number, final int size, final SortingData sortingData, final List<T> content,
                     final boolean hasNext) {
        super(number, size, sortingData, content);
        this.hasNext = hasNext;
    }


    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public <U> Slice<U> map(final Function<T, U> mapper) {
        final var mapped = content().stream().map(mapper).collect(Collectors.toList());
        return new SliceImpl<>(number(), size(), sortingData(), mapped, hasNext);
    }
}
