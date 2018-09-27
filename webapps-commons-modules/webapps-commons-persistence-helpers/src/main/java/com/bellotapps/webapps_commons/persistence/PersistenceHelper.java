/*
 * Copyright 2018 BellotApps
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

package com.bellotapps.webapps_commons.persistence;


import com.bellotapps.webapps_commons.exceptions.InvalidPropertiesException;
import org.hibernate.criterion.MatchMode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Class implementing helper methods for the persistence layer.
 */
public class PersistenceHelper {

    /**
     * Creates a {@link Predicate} representing a "like",
     * which matches the given {@code attributeName}
     * (of the {@link EntityType} got by executing {@link Root#getModel()} from the given {@link Root})
     * with the given {@code pattern}.
     *
     * @param cb            The {@link CriteriaBuilder} used to build the {@link Predicate}.
     * @param root          The {@link Root} from where the {@link Predicate} will be used.
     * @param attributeName The attribute name from the given {@link EntityType} to match
     * @param pattern       The pattern to match.
     * @param matchMode     The {@link MatchMode} to be applied (i.e exact, start, end or anywhere).
     * @param caseSensitive A flag indicating if the match must be case sensitive.
     * @param <T>           The concrete type of {@link EntityType} to which this predicate is applied.
     * @return The resulting {@link Predicate}.
     */
    public static <T> Predicate toLikePredicate(final CriteriaBuilder cb, final Root<T> root,
                                                final String attributeName, final String pattern,
                                                final MatchMode matchMode, final boolean caseSensitive) {
        Assert.notNull(cb, "The CriteriaBuilder must not be null");
        Assert.notNull(root, "The Root must not be null");
        Assert.notNull(attributeName, "The attribute name must not be null");
        Assert.notNull(pattern, "The pattern must not be null");
        Assert.notNull(matchMode, "The MatchMode must not be null");

        final Path<String> attributePath = root.get(root.getModel()
                .getDeclaredSingularAttribute(attributeName, String.class));
        return caseSensitive ?
                cb.like(attributePath, matchMode.toMatchString(pattern)) :
                cb.like(cb.lower(attributePath), matchMode.toMatchString(pattern.toLowerCase()));
    }

    /**
     * Validates that the given {@link Pageable} is valid for querying entities of the given {@code klass}.
     *
     * @param pageable The {@link Pageable} to be validated.
     * @param klass    The class representing the entity.
     * @param <T>      The concrete type of the entities.
     * @throws InvalidPropertiesException If it has a {@link org.springframework.data.domain.Sort}
     *                                    with invalid properties.
     */
    public static <T> void validatePageable(final Pageable pageable, final Class<T> klass)
            throws InvalidPropertiesException {
        final var properties = Arrays.stream(klass.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());
        final var spliterator = Spliterators.spliteratorUnknownSize(pageable.getSort().iterator(), Spliterator.ORDERED);
        final var invalidProperties = StreamSupport.stream(spliterator, false)
                .map(Sort.Order::getProperty)
                .filter(property -> !properties.contains(property))
                .collect(Collectors.toList());
        if (!invalidProperties.isEmpty()) {
            throw new InvalidPropertiesException(invalidProperties);
        }
    }
}
