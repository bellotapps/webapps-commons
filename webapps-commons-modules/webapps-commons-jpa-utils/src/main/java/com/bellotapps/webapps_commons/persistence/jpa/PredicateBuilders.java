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

package com.bellotapps.webapps_commons.persistence.jpa;

import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import java.util.function.BiFunction;

/**
 * Class holding several predicate builder methods.
 */
public class PredicateBuilders {

    /**
     * Creates a {@link Predicate} representing a "like",
     * which matches the given {@code attributeName}
     * (of the {@link EntityType} got by executing {@link Root#getModel()} from the given {@link Root})
     * with the given {@code pattern}.
     *
     * @param criteriaBuilder The {@link CriteriaBuilder} to be used.
     * @param root            The {@link Root} from where the {@link Predicate} will be built.
     * @param attributeName   The name of the attribute the {@link Predicate} makes reference.
     * @param pattern         The pattern to match.
     * @param matchMode       The {@link LikeMatchMode} to be applied (i.e exact, start, end or anywhere).
     * @param caseSensitive   A flag indicating if the match must be case sensitive.
     * @param <T>             The concrete type of {@link EntityType} to which this predicate is applied.
     * @return The resulting {@link Predicate}.
     */
    public static <T> Predicate like(final CriteriaBuilder criteriaBuilder, final Root<T> root,
                                     final String attributeName, final String pattern,
                                     final LikeMatchMode matchMode, final boolean caseSensitive) {
        Assert.notNull(pattern, "The pattern must not be null");
        Assert.notNull(matchMode, "The MatchMode must not be null");
        return singleAttributePredicate(criteriaBuilder, root, attributeName, String.class,
                (cb, path) -> caseSensitive ?
                        cb.like(path, matchMode.toMatchString(pattern)) :
                        cb.like(cb.lower(path), matchMode.toMatchString(pattern.toLowerCase()))
        );
    }

    /**
     * Creates a {@link Predicate} to test equality.
     *
     * @param criteriaBuilder The {@link CriteriaBuilder} to be used.
     * @param root            The {@link Root} from where the {@link Predicate} will be built.
     * @param attributeName   The name of the attribute the {@link Predicate} makes reference.
     * @param filterClass     The {@link Class} of the attribute.
     * @param filter          Object to which equality will be compared against to.
     * @param <T>             The concrete type of {@link EntityType} to which this predicate is applied.
     * @param <F>             The concrete type of the attribute the {@link Predicate} makes reference.
     * @return The resulting {@link Predicate}.
     */
    public static <T, F> Predicate equality(final CriteriaBuilder criteriaBuilder, final Root<T> root,
                                            final String attributeName, final Class<F> filterClass, final F filter) {
        Assert.notNull(filter, "The object to which equality will be compared against to must not be null");
        return singleAttributePredicate(criteriaBuilder, root, attributeName, filterClass,
                (cb, path) -> cb.equal(path, filter)
        );
    }


    /**
     * Builds a {@link Predicate} for a {@link javax.persistence.metamodel.SingularAttribute}.
     *
     * @param builder           The {@link CriteriaBuilder} to be used.
     * @param root              The {@link Root} from where the {@link Predicate} will be built.
     * @param attributeName     The name of the attribute the {@link Predicate} makes reference.
     * @param filterClass       The {@link Class} of the attribute.
     * @param predicateProvider A {@link BiFunction} that takes the given {@link CriteriaBuilder}
     *                          and the {@link Path} of the attribute, and provides the {@link Predicate}
     *                          to be returned.
     *                          This function can contain objects to which comparisons might be performed if needed.
     * @param <T>               The concrete type of {@link EntityType} to which this predicate is applied.
     * @param <F>               The concrete type of the attribute the {@link Predicate} makes reference.
     * @return The build {@link Predicate}.
     */
    public static <T, F>
    Predicate singleAttributePredicate(
            final CriteriaBuilder builder, final Root<T> root, final String attributeName, final Class<F> filterClass,
            final BiFunction<CriteriaBuilder, Path<F>, Predicate> predicateProvider) {

        // First, validate elements
        Assert.notNull(builder, "The CriteriaBuilder must not be null");
        Assert.notNull(root, "The Root must not be null");
        Assert.notNull(attributeName, "The attribute name must not be null");
        Assert.notNull(filterClass, "The filter class must not be null");
        Assert.notNull(predicateProvider, "The predicateProvider must not be null");

        // Start by obtaining the attribute
        final var attribute = root.getModel().getDeclaredSingularAttribute(attributeName, filterClass);
        // Then, get the path to the attribute
        final var path = root.get(attribute);
        // Finally, obtain the Predicate from the CriteriaBuilder and the path.
        return predicateProvider.apply(builder, path);
    }


    /**
     * Strategies for matching strings using "like".
     */
    public enum LikeMatchMode {

        /**
         * Matches the entire string.
         */
        EXACT {
            @Override
            String toMatchString(final String pattern) {
                return pattern;
            }
        },

        /**
         * Matches the start of the string.
         */
        START {
            @Override
            String toMatchString(final String pattern) {
                return pattern + '%';
            }
        },

        /**
         * Matches the end of the string.
         */
        END {
            @Override
            String toMatchString(final String pattern) {
                return '%' + pattern;
            }
        },

        /**
         * Matches anywhere in the string.
         */
        ANYWHERE {
            @Override
            String toMatchString(final String pattern) {
                return '%' + pattern + '%';
            }
        };

        /**
         * Convert the pattern, by appending/prepending "%"
         *
         * @param pattern The pattern for convert according to the mode
         * @return The converted pattern
         */
        /* package */
        abstract String toMatchString(final String pattern);
    }
}
