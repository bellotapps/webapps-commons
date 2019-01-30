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

package com.bellotapps.webapps_commons.data_transfer.base64;

import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * Helps the process of applying base64url encoding and decoding.
 */
public class Base64UrlHelper {

    /**
     * Performs base64url encoding of the given {@code rawNumber}.
     *
     * @param rawNumber                     The {@link Number} to encode.
     * @param toNotYetEncodedStringFunction {@link Function} from {@link String} to {@code T}
     *                                      that transforms a base64url decoded {@link String}
     *                                      into an object of type {@code T}.
     * @param <T>                           The concrete subtype of {@link Number}.
     * @return The encoded {@link Number}.
     */
    public static <T extends Number> String encodeFromNumber(final T rawNumber,
                                                             final Function<T, String> toNotYetEncodedStringFunction) {
        try {
            return toNotYetEncodedStringFunction
                    .andThen(String::getBytes)
                    .andThen(Base64Utils::encodeToUrlSafeString)
                    .apply(rawNumber);
        } catch (final Throwable e) {
            throw new Base64UrlException("Could not encode value " + rawNumber.toString(), e);
        }
    }

    /**
     * Performs decoding into a {@link Number}.
     *
     * @param encodedNumber             The {@link String} representing a {@link Number} in base64url format.
     * @param fromDecodedStringFunction {@link Function} from  {@code T} to {@link String}
     *                                  that transforms an object of type {@code T}
     *                                  into the {@link String} that will be encoded with base64url.
     * @param <T>                       The concrete subtype of {@link Number}.
     * @return The decoded {@link Number}.
     */
    public static <T extends Number> T decodeToNumber(final String encodedNumber,
                                                      final Function<String, T> fromDecodedStringFunction) {
        try {
            return fromDecodedStringFunction
                    .compose(Base64UrlHelper::fromByteArray)
                    .compose(Base64Utils::decodeFromUrlSafeString)
                    .apply(encodedNumber);
        } catch (final IllegalArgumentException e) {
            throw new Base64UrlException("Could not decode value " + encodedNumber, e);
        }
    }

    /**
     * Transforms the given {@code byte} {@code array} into a {@link String},
     * using {@link StandardCharsets#UTF_8} encoding.
     *
     * @param array The array of {@code byte} from where the {@link String} will be created.
     * @return The created {@link String}.
     */
    private static String fromByteArray(final byte[] array) {
        return new String(array, StandardCharsets.UTF_8);
    }

    /**
     * Exception to be thrown when a base64url encoding or decoding operation fails.
     */
    private static final class Base64UrlException extends RuntimeException {

        /**
         * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
         * @param cause   The cause (which is saved for later retrieval by the {@link #getCause()} method).
         *                For more information, see {@link RuntimeException#RuntimeException(Throwable)}.
         */
        private Base64UrlException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
