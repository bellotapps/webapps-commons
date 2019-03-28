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

package com.bellotapps.webapps_commons.persistence.spring_data.repository_utils_adapters.repositories;

import com.bellotapps.webapps_commons.persistence.repository_utils.paging_and_sorting.Page;
import com.bellotapps.webapps_commons.persistence.repository_utils.paging_and_sorting.PagingRequest;
import com.bellotapps.webapps_commons.persistence.repository_utils.repositories.PagingRepository;
import com.bellotapps.webapps_commons.persistence.spring_data.repository_utils_adapters.paging_and_sorting.PagingMapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * An extension of a {@link PagingRepository}
 * which implements all its methods using defaults,
 * and delegating paged read operations to the {@link PagingAndSortingRepository} obtained
 * by the {@link #getPagingAndSortingRepository()} method.
 *
 * @param <E>  Concrete type of the entity to be managed by the repository.
 * @param <ID> Concrete type of the entity's id.
 */
public interface PagingRepositoryAdapter<E, ID>
        extends PagingRepository<E, ID>, PagingAndSortingRepositoryAdapter<E, ID> {

    /**
     * Provides the repository to which write operations are delegated.
     *
     * @return a {@link CrudRepository} to which write operations are delegated.
     */
    PagingAndSortingRepository<E, ID> getPagingAndSortingRepository();

    /**
     * Returns a {@link Page} of entities according to the given {@code pagingRequest}.
     *
     * @param pagingRequest The {@link PagingRequest} that indicates page number, size, sorting options, etc.
     * @return A {@link Page} of entities.
     */
    @Override
    default Page<E> findAll(final PagingRequest pagingRequest) {
        final var pageable = PagingMapper.map(pagingRequest);
        final var page = getPagingAndSortingRepository().findAll(pageable);
        return PagingMapper.map(page);
    }
}
