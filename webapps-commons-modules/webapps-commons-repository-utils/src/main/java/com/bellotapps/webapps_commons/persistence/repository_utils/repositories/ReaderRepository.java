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

package com.bellotapps.webapps_commons.persistence.repository_utils.repositories;


import java.util.Optional;

/**
 * Interface for a repository with basic reading operations.
 *
 * @param <E>  Concrete type of the entity to be managed by the repository.
 * @param <ID> Concrete type of the entity's id.
 */
public interface ReaderRepository<E, ID> {

    /**
     * Returns the amount of entities available in the repository.
     *
     * @return The amount of entities available.
     */
    long count();

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id The id of the entity to check existence.
     * @return {@code true} if an entity with the given {@code id} exists, or {@code false} otherwise.
     * @throws IllegalArgumentException If the given {@code id} is {@code null}.
     */
    boolean existsById(final ID id) throws IllegalArgumentException;

    /**
     * Finds the entity with the given {@code id}.
     *
     * @param id The id of the entity to be returned.
     * @return An {@link Optional} that contains the entity with the given {@code id} if it exists, or empty otherwise.
     * @throws IllegalArgumentException If the given {@code id} is {@code null}.
     */
    Optional<E> findById(final ID id) throws IllegalArgumentException;

    /**
     * Returns all the entities in the repository.
     *
     * @return All the entities.
     */
    Iterable<E> findAll();
}
