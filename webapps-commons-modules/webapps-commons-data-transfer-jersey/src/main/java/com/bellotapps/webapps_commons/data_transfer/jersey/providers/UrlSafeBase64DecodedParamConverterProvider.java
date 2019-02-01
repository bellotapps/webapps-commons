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

import com.bellotapps.webapps_commons.data_transfer.base64.Base64UrlHelper;
import com.bellotapps.webapps_commons.data_transfer.jersey.annotations.Base64url;
import org.springframework.util.ClassUtils;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * The {@link ParamConverterProvider} for url safe base64 encoded params.
 */
@Provider
public class UrlSafeBase64DecodedParamConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(
            final Class<T> rawType,
            final Type genericType,
            final Annotation[] annotations) {

        // First, check if rawType implements Number (i.e Number is assignable from rawType)
        if (!ClassUtils.isAssignable(Number.class, rawType)) {
            return null;
        }

        // We know that the rawType can be assigned to a Number,
        // so we know that it can be casted to a Class of something extending Number
        @SuppressWarnings("unchecked") final var castedRawType = (Class<? extends Number>) rawType;

        // We now have to check if the the Base64url annotation is in the array,
        // and create the corresponding UrlSafeBase64EncodedParamConverter with the createFor method
        // (will supply only for supported Number, returning null for those that are not supported).
        // In case there is no Base64url annotation in the array, or the Number is not supported,
        // then the Optional will become an empty Optional, returning null.
        return ParamConverterProvidersHelper.getAnnotationOfType(annotations, Base64url.class)
                .map(ignored -> UrlSafeBase64EncodedParamConverter.createFor(castedRawType))
                .map(ParamConverterProvidersHelper::<T, UrlSafeBase64EncodedParamConverter<?>>castedParamConverter)
                .orElse(null);

    }

    /**
     * A {@link ParamConverter} from a url safe base 64 encoded value into any subclass of {@link Number},
     *
     * @param <T> The concrete subclass of {@link Number}.
     */
    private static final class UrlSafeBase64EncodedParamConverter<T extends Number> implements ParamConverter<T> {


        /**
         * {@link Function} from {@link String} to {@code T} that transforms a base64url decoded {@link String}
         * into an object of type {@code T}.
         */
        private final Function<String, T> fromDecodedStringFunction;

        /**
         * {@link Function} from  {@code T} to {@link String} that transforms an object of type {@code T}
         * into the {@link String} that will be encoded with base64url.
         */
        private final Function<T, String> toNotYetEncodedStringFunction;

        /**
         * Constructor.
         *
         * @param fromDecodedStringFunction     {@link Function} from {@link String} to {@code T}
         *                                      that transforms a base64url decoded {@link String}
         *                                      into an object of type {@code T}.
         * @param toNotYetEncodedStringFunction {@link Function} from  {@code T} to {@link String}
         *                                      that transforms an object of type {@code T}
         *                                      into the {@link String} that will be encoded with base64url.
         */
        private UrlSafeBase64EncodedParamConverter(final Function<String, T> fromDecodedStringFunction,
                                                   final Function<T, String> toNotYetEncodedStringFunction) {
            this.fromDecodedStringFunction = fromDecodedStringFunction;
            this.toNotYetEncodedStringFunction = toNotYetEncodedStringFunction;
        }

        @Override
        public T fromString(final String value) {
            return Base64UrlHelper.decodeToNumber(value, fromDecodedStringFunction);
        }

        @Override
        public String toString(final T value) {
            return Base64UrlHelper.encodeFromNumber(value, toNotYetEncodedStringFunction);
        }

        /**
         * Creates a {@link UrlSafeBase64EncodedParamConverter} for the given {@code rawType}.
         *
         * @param rawType The {@link Class} for which the {@link UrlSafeBase64EncodedParamConverter} will be created.
         * @param <E>     The concrete type for which the {@link UrlSafeBase64EncodedParamConverter} will be created.
         * @return The created {@link UrlSafeBase64EncodedParamConverter}.
         */
        private static <E extends Number> UrlSafeBase64EncodedParamConverter<E> createFor(final Class<E> rawType) {

            if (rawType == Long.class) {
                @SuppressWarnings("unchecked") final var converter =
                        (UrlSafeBase64EncodedParamConverter<E>)
                                new UrlSafeBase64EncodedParamConverter<>(Long::valueOf, Object::toString);
                return converter;
            }
            if (rawType == Integer.class) {
                @SuppressWarnings("unchecked") final var converter =
                        (UrlSafeBase64EncodedParamConverter<E>)
                                new UrlSafeBase64EncodedParamConverter<>(Integer::valueOf, Object::toString);
                return converter;
            }
            if (rawType == Short.class) {
                @SuppressWarnings("unchecked") final var converter =
                        (UrlSafeBase64EncodedParamConverter<E>)
                                new UrlSafeBase64EncodedParamConverter<>(Short::valueOf, Object::toString);
                return converter;
            }
            if (rawType == Byte.class) {
                @SuppressWarnings("unchecked") final var converter =
                        (UrlSafeBase64EncodedParamConverter<E>)
                                new UrlSafeBase64EncodedParamConverter<>(Byte::valueOf, Object::toString);
                return converter;
            }
            // If reached here, the given class that can be assigned to a Number is not supported.
            return null;
        }
    }
}
