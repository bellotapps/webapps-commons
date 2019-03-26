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

package com.bellotapps.webapps_commons.errors;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class representing an error that occurs when violating a value constraint on an entity.
 * There are two types of validation errors: a mandatory value is missing, or a value is not a legal one
 * (see {@link ErrorCause} for more information).
 */
public final class ConstraintViolationError extends EntityError {

    /**
     * The {@link ErrorCause} representing what caused the new error.
     */
    private final ErrorCause cause;

    /**
     * The fields that did not validate.
     */
    private final String field;


    /**
     * Constructor.
     *
     * @param cause   The {@link ErrorCause} representing what caused the new error.
     * @param field   The field that did not validate.
     * @param message A helper message to be used for debugging purposes.
     */
    public ConstraintViolationError(final ErrorCause cause, final String field, final String message) {
        super(message);
        this.cause = cause;
        this.field = field;
    }


    /**
     * @return The {@link ErrorCause} representing what caused the new error.
     */
    public ErrorCause getCause() {
        return cause;
    }

    /**
     * @return The fields that did not validate.
     */
    public String getField() {
        return field;
    }

    /**
     * Creates a {@link ConstraintViolationError} from a {@link ConstraintViolation}.
     *
     * @param violation The {@link ConstraintViolation} from where the error will be built.
     * @return The created {@link ConstraintViolationError}.
     * @apiNote To use this method, the given {@code violation}
     * must have one (and only one) payload of type {@link ErrorCausePayload}
     * (it can have more than one payload, but just one of type {@link ErrorCausePayload}).
     * In case there is more than one {@link ErrorCausePayload}, unexpected behaviour might occur.
     */
    public static ConstraintViolationError fromConstraintViolation(final ConstraintViolation<?> violation) {
        final var errorCause = violation.getConstraintDescriptor()
                .getPayload()
                .stream()
                .filter(klass -> ClassUtils.isAssignable(ErrorCausePayload.class, klass)) // Only ErrorCausePayloads
                .findFirst() // Just the first one (more than one: unexpected behaviour, as only the first one is used).
                .map(ErrorCause::getByPayloadClass) // Map to an ErrorCause value
                .orElse(ErrorCause.UNKNOWN); // Unknown error cause if no payload of type ErrorCausePayload.
        final var path = StringUtils.delimitedListToStringArray(violation.getPropertyPath().toString(), ".", null);
        final var message = violation.getMessageTemplate();
        return new ConstraintViolationError(errorCause, path[path.length - 1], message);
    }


    /**
     * En enum indicating types of validation errors.
     */
    public enum ErrorCause implements Payload {
        /**
         * Error that occurs when a mandatory value is missing (i.e was set to {@code null}).
         */
        MISSING_VALUE(ErrorCausePayload.MissingValue.class),
        /**
         * Error that occurs when a value does not match the constraints established for it.
         */
        ILLEGAL_VALUE(ErrorCausePayload.IllegalValue.class),
        /**
         * Unknown error (should not be used, it only exists as a fallback).
         */
        UNKNOWN(Payload.class);

        /**
         * {@link Map} holding, for each {@link Payload} class, the corresponding {@link ErrorCause}.
         */
        private final static Map<Class<? extends Payload>, ErrorCause> byErrorCausePayload =
                Stream.of(MISSING_VALUE, ILLEGAL_VALUE)
                        .collect(Collectors.toMap(ErrorCause::getMapping, Function.identity()));

        /**
         * Maps a {@link Payload} class into an {@link ErrorCause}.
         *
         * @param payloadClass The {@link Payload} class.
         * @return The corresponding {@link ErrorCause} for the given {@link Payload} class.
         */
        private static ErrorCause getByPayloadClass(final Class<? extends Payload> payloadClass) {
            Assert.notNull(payloadClass, "The payload class must not be null");
            return byErrorCausePayload.getOrDefault(payloadClass, UNKNOWN);
        }

        /**
         * Holds a reference to the {@link Payload} class that maps to this {@link ErrorCause}.
         */
        private final Class<? extends Payload> mapsTo;

        /**
         * Constructor.
         *
         * @param mapsTo The {@link Payload} class that maps to this {@link ErrorCause}.
         */
        ErrorCause(Class<? extends Payload> mapsTo) {
            this.mapsTo = mapsTo;
        }

        /**
         * @return The {@link Payload} class that maps to this {@link ErrorCause}.
         */
        private Class<? extends Payload> getMapping() {
            return mapsTo;
        }
    }

    /**
     * A generic implementation of {@link Payload}, used for identifying {@link ErrorCause}s.
     */
    public static class ErrorCausePayload implements Payload {


        /**
         * Private constructor (to avoid instantiation).
         */
        private ErrorCausePayload() {
        }

        /**
         * An extension of {@link ErrorCausePayload} used to identify a constraint violation given by a missing value
         * (i.e a {@code null} value when it was not expected).
         */
        public static class MissingValue extends ErrorCausePayload {

            /**
             * Private constructor (to avoid instantiation).
             */
            private MissingValue() {
            }
        }

        /**
         * An extension of {@link ErrorCausePayload} used to identify a constraint violation given by an illegal value
         * (e.g future birth-dates, or negative quantities).
         */
        public static class IllegalValue extends ErrorCausePayload {

            /**
             * Private constructor (to avoid instantiation).
             */
            private IllegalValue() {
            }
        }
    }
}
