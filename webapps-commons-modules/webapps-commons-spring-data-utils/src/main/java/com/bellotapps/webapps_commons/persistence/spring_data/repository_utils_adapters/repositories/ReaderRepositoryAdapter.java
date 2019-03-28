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

import com.bellotapps.webapps_commons.persistence.repository_utils.repositories.ReaderRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * An extension of a {@link ReaderRepository}
 * which implements all its methods using defaults,
 * and delegating read operations to the {@link CrudRepository} obtained
 * by the {@link #getCrudRepository()} method.
 *
 * @param <E>  Concrete type of the entity to be managed by the repository.
 * @param <ID> Concrete type of the entity's id.
 */
public interface ReaderRepositoryAdapter<E, ID> extends ReaderRepository<E, ID>, CrudRepositoryAdapter<E, ID> {

    /**
     * Returns the amount of entities available in the repository.
     *
     * @return The amount of entities available.
     */
    @Override
    default long count() {
        return getCrudRepository().count();
    }

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id The id of the entity to check existence.
     * @return {@code true} if an entity with the given {@code id} exists, or {@code false} otherwise.
     * @throws IllegalArgumentException If the given {@code id} is {@code null}.
     */
    @Override
    default boolean existsById(final ID id) throws IllegalArgumentException {
        return getCrudRepository().existsById(id);
    }

    /**
     * Finds the entity with the given {@code id}.
     *
     * @param id The id of the entity to be returned.
     * @return An {@link Optional} that contains the entity with the given {@code id} if it exists, or empty otherwise.
     * @throws IllegalArgumentException If the given {@code id} is {@code null}.
     */
    @Override
    default Optional<E> findById(final ID id) throws IllegalArgumentException {
        return getCrudRepository().findById(id);
    }

    /**
     * Returns all the entities in the repository.
     *
     * @return All the entities.
     */
    @Override
    default Iterable<E> findAll() {
        return getCrudRepository().findAll();
    }
}
