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

package com.bellotapps.webapps_commons.validation.jersey;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * A {@link ConstraintViolationException} creator used by the {@link JerseyValidator} to create instances of
 * {@link ConstraintViolationException} to be thrown when the validation fails.
 */
@FunctionalInterface
public interface ConstraintViolationExceptionCreator {

    /**
     * Creates a {@link ConstraintViolationException} from the given {@link Set} of {@link ConstraintViolation}s.
     *
     * @param violations A {@link Set} containing the {@link ConstraintViolation} that causes the creation of the
     *                   {@link ConstraintViolationException} to be thrown.
     * @param <T>        The type of the root bean violating constraints.
     * @return The created {@link ConstraintViolationException}.
     * @apiNote It can return a {@link ConstraintViolationException} or any subclass.
     */
    <T> ConstraintViolationException create(final Set<ConstraintViolation<T>> violations);
}
