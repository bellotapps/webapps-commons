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

package com.bellotapps.webapps_commons.data_transfer.jersey.providers;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;

/**
 * Helper class for {@link ParamConverterProvider}s defined in this library..
 */
/* package */ class ParamConverterProvidersHelper {

    /**
     * Casts the given {@code converter} into a generic {@link ParamConverter}.
     *
     * @param converter The concrete {@link ParamConverter}.
     * @param <T>       The generic type of the resulting {@link ParamConverter}.
     * @param <C>       The concrete type of the given {@code converter}.
     * @return The generic {@link ParamConverter}.
     */
    /* package */
    static <T, C extends ParamConverter<?>> ParamConverter<T> castedParamConverter(final C converter) {
        @SuppressWarnings("unchecked") final var castedConverter = (ParamConverter<T>) converter;
        return castedConverter;
    }

    /**
     * Retrieves an {@link Optional} of type {@code A} (whose class if the given {@code annotationClass})
     * from the given {@code annotations} array.
     *
     * @param annotations     The {@link Annotation} array
     *                        from where the {@link Annotation} of type {@code A} will be retrieved.
     * @param annotationClass The class of the {@link Annotation} of type {@code A}.
     * @param <A>             The concrete type of the {@link Annotation}.
     * @return An {@link Optional} of {@code A} that will hold the {@link Annotation} is present in the array,
     * or empty if there is no {@link Annotation} of type {@code A}.
     */
    /* package */
    static <A extends Annotation> Optional<A> getAnnotationOfType(Annotation[] annotations, Class<A> annotationClass) {
        return Arrays.stream(annotations)
                .filter(annotation -> annotationClass.equals(annotation.annotationType()))
                .findFirst()
                .map(annotationClass::cast);
    }
}
