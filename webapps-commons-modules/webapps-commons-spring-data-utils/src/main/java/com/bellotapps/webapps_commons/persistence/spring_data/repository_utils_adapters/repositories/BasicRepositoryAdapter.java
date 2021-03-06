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

import com.bellotapps.webapps_commons.persistence.repository_utils.repositories.BasicRepository;

/**
 * An extension of a {@link BasicRepository}
 * which also extends {@link ReaderRepositoryAdapter} and a {@link WriterRepositoryAdapter}.
 *
 * @param <E>  Concrete type of the entity to be managed by the repository.
 * @param <ID> Concrete type of the entity's id.
 */
public interface BasicRepositoryAdapter<E, ID>
        extends BasicRepository<E, ID>, ReaderRepositoryAdapter<E, ID>, WriterRepositoryAdapter<E, ID> {
}
