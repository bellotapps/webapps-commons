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


import com.bellotapps.webapps_commons.data_transfer.date_time.DateTimeFormatters;
import com.bellotapps.webapps_commons.data_transfer.jersey.annotations.Java8Time;
import org.springframework.util.ClassUtils;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import java.util.function.Function;

/**
 * The {@link ParamConverterProvider} for Java 8 time types.
 */
@Provider
public class Java8TimeParamConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(
            final Class<T> rawType,
            final Type genericType,
            final Annotation[] annotations) {

        // First, check if rawType implements TemporalAccessor (i.e TemporalAccessor is assignable from rawType)
        if (!ClassUtils.isAssignable(TemporalAccessor.class, rawType)) {
            return null;
        }

        // We know that the rawType can be assigned to a TemporalAccessor,
        // so we know that it can be casted to a Class of something extending TemporalAccessor
        @SuppressWarnings("unchecked") final var castedRawType = (Class<? extends TemporalAccessor>) rawType;

        // We now have to get the Java8Time annotation from the annotations array,
        // and create the corresponding Java8TimeParamConverter with the createFor method
        // (will supply only for supported TemporalAccessors, returning null for those that are not supported).
        // In case there is no Java8Time annotation in the array, or the TemporalAccessor is not supported,
        // then the Optional will become an empty Optional, returning null.
        return ParamConverterProvidersHelper
                .getAnnotationOfType(annotations, Java8Time.class)
                .map(Java8Time::formatter)
                .map(DateTimeFormatters::getFormatter)
                .map(formatter -> Java8TimeParamConverter.createFor(castedRawType, formatter))
                .map(ParamConverterProvidersHelper::<T, Java8TimeParamConverter<?>>castedParamConverter)
                .orElse(null);
    }


    /**
     * A {@link ParamConverter} for any implementation of {@link TemporalAccessor} that can be obtained
     * by a {@link Function} that receives a {@link TemporalAccessor}.
     *
     * @param <E> The type that can be obtained by applying a {@link Function} to a {@link TemporalAccessor}.
     */
    private static final class Java8TimeParamConverter<E extends TemporalAccessor> implements ParamConverter<E> {

        /**
         * The {@link DateTimeFormatter} to use to create a {@link TemporalAccessor}
         * to which the {@link #creatorFunction} will be applied.
         */
        private final DateTimeFormatter formatter;

        /**
         * A {@link Function} that creates an object of type {@code E} from a {@link TemporalAccessor}.
         */
        private final Function<TemporalAccessor, E> creatorFunction;

        /**
         * Constructor.
         *
         * @param formatter       The {@link DateTimeFormatter} to use to create a {@link TemporalAccessor}
         *                        to which the {@link #creatorFunction} will be applied.
         * @param creatorFunction A {@link Function} that creates an object of type {@code E}
         *                        from a {@link TemporalAccessor}.
         */
        private Java8TimeParamConverter(
                final DateTimeFormatter formatter,
                final Function<TemporalAccessor, E> creatorFunction) {
            this.formatter = formatter;
            this.creatorFunction = creatorFunction;
        }

        @Override
        public E fromString(final String value) {
            return Optional.ofNullable(value)
                    .map(formatter::parse)
                    .map(creatorFunction)
                    .orElse(null);
        }

        @Override
        public String toString(final E value) {
            return formatter.format(value);
        }


        /**
         * Creates a {@link Java8TimeParamConverter} for the given {@code rawType}, using the given {@code formatter}.
         *
         * @param rawType   The {@link Class} for which the {@link Java8TimeParamConverter} will be created.
         * @param formatter The {@link DateTimeFormatter}
         *                  to be passed to the {@link Java8TimeParamConverter} constructor.
         * @param <E>       The concrete type for which the {@link Java8TimeParamConverter} will be created.
         * @return The created {@link Java8TimeParamConverter}.
         */
        private static <E extends TemporalAccessor> Java8TimeParamConverter<E> createFor(
                final Class<E> rawType,
                final DateTimeFormatter formatter) {

            if (rawType == LocalDate.class) {
                @SuppressWarnings("unchecked") final var converter =
                        (Java8TimeParamConverter<E>) new Java8TimeParamConverter<>(formatter, LocalDate::from);
                return converter;
            }
            if (rawType == LocalTime.class) {
                @SuppressWarnings("unchecked") final var converter =
                        (Java8TimeParamConverter<E>) new Java8TimeParamConverter<>(formatter, LocalTime::from);
                return converter;
            }
            if (rawType == LocalDateTime.class) {
                @SuppressWarnings("unchecked") final var converter =
                        (Java8TimeParamConverter<E>) new Java8TimeParamConverter<>(formatter, LocalDateTime::from);
                return converter;
            }

            // If reached here, the given class that can be assigned to a TemporalAccessor is not supported.
            return null;
        }
    }
}
