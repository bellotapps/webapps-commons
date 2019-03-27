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

package com.bellotapps.webapps_commons.persistence.spring_data.repository_utils_adapters.repositories;

import com.bellotapps.webapps_commons.persistence.repository_utils.paging_and_sorting.SortingData;
import com.bellotapps.webapps_commons.persistence.repository_utils.repositories.SortingRepository;
import com.bellotapps.webapps_commons.persistence.spring_data.repository_utils_adapters.paging_and_sorting.SortingMapper;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * An extension of a {@link SortingRepository}
 * which implements all its methods using defaults,
 * and delegating sorted read operations to the {@link PagingAndSortingRepository} obtained
 * by the {@link #getPagingAndSortingRepository()} method.
 *
 * @param <E>  Concrete type of the entity to be managed by the repository.
 * @param <ID> Concrete type of the entity's id.
 */
public interface SortingRepositoryAdapter<E, ID>
        extends SortingRepository<E, ID>, PagingAndSortingRepositoryAdapter<E, ID> {

    /**
     * Returns all entities using the given {@code sortingData}.
     *
     * @param sortingData A {@link SortingData} object that indicates how sorting must be performed.
     * @return All entities, applying sorting according to the given {@code sortingData}.
     */
    @Override
    default Iterable<E> findAll(final SortingData sortingData) {
        return getPagingAndSortingRepository().findAll(SortingMapper.map(sortingData));
    }
}
