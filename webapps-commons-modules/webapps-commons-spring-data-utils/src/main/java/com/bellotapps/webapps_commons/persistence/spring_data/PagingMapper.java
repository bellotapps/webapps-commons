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

package com.bellotapps.webapps_commons.persistence.spring_data;

import com.bellotapps.webapps_commons.persistence.repository_utils.Page;
import com.bellotapps.webapps_commons.persistence.repository_utils.PagingRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * A mapper for paging information.
 */
public class PagingMapper {

    /**
     * Maps a {@link PagingRequest} into a {@link Pageable}.
     *
     * @param pagingRequest The {@link PagingRequest} to be mapped into a {@link Pageable}.
     * @return A {@link Pageable} built from the information of the given {@code pagingRequest}.
     */
    public static Pageable map(final PagingRequest pagingRequest) {
        return PageRequest.of(
                pagingRequest.pageNumber(),
                pagingRequest.pageSize(),
                SortingMapper.map(pagingRequest.sortBy())
        );
    }

    /**
     * Maps a {@link Pageable} into a {@link PagingRequest}.
     *
     * @param pageable The {@link Pageable} to be mapped into a {@link PagingRequest}.
     * @return A {@link PagingRequest} built from the information of the given {@code pageable}.
     */
    public static PagingRequest map(final Pageable pageable) {
        return PagingRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                SortingMapper.map(pageable.getSort())
        );
    }

    /**
     * Maps a webapps commons {@link Page} into a Spring Data {@link org.springframework.data.domain.Page}.
     *
     * @param page The {@link Page} to be mapped.
     * @param <T>  The type of the content in the page.
     * @return The mapped {@link org.springframework.data.domain.Page}.
     */
    public static <T> org.springframework.data.domain.Page<T> map(final Page<T> page) {
        return new PageImpl<>(
                page.content(),
                map(page.requestedBy()),
                page.totalElements()
        );
    }

    /**
     * Maps a Spring Data {@link org.springframework.data.domain.Page} into a webapps commons {@link Page}.
     *
     * @param page The {@link org.springframework.data.domain.Page} to be mapped.
     * @param <T>  The type of the content in the page.
     * @return The mapped {@link Page}.
     */
    public static <T> Page<T> map(final org.springframework.data.domain.Page<T> page) {
        return Page.of(
                page.getNumber(),
                page.getSize(),
                SortingMapper.map(page.getSort()),
                page.getContent(),
                page.getTotalElements()
        );
    }
}
