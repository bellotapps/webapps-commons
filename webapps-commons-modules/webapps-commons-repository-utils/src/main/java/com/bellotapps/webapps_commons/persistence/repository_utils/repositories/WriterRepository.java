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


/**
 * Interface for a repository with writing operations.
 *
 * @param <E>  Concrete type of the entity to be managed by the repository.
 * @param <ID> Concrete type of the entity's id.
 */
public interface WriterRepository<E, ID> {

    /**
     * Saves the given {@code entity}. Use the returned entity instance for further operation.
     *
     * @param entity The entity to be saved.
     * @param <S>    Concrete type of the entity.
     * @return The saved instance.
     * @throws IllegalArgumentException If the given {@code entity} is {@code null}.
     */
    <S extends E> S save(final S entity) throws IllegalArgumentException;

    /**
     * Deletes the given {@code entity}.
     *
     * @param entity The entity to be deleted.
     * @param <S>    Concrete type of the entity.
     * @throws IllegalArgumentException If the given {@code entity} is {@code null}.
     */
    <S extends E> void delete(final S entity) throws IllegalArgumentException;

    /**
     * Deletes the entity with the given {@code id}.
     *
     * @param id The id of the entity to be deleted.
     * @throws IllegalArgumentException If the given {@code id} is {@code null}.
     */
    void deleteById(final ID id) throws IllegalArgumentException;

    /**
     * Deletes all the entities in the repository.
     */
    void deleteAll();
}
