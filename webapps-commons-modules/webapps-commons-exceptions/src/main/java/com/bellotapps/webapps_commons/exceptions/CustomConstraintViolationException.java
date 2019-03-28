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

package com.bellotapps.webapps_commons.exceptions;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * A custom extension of {@link ConstraintViolationException} created in order to be thrown/handled in a custom way
 * (having separated handling strategies for this extension and the parent type).
 */
public class CustomConstraintViolationException extends ConstraintViolationException {

    /**
     * Creates a custom constraint violation report.
     *
     * @param message              The error message.
     * @param constraintViolations A {@code Set} of {@link ConstraintViolation}s or null.
     */
    public CustomConstraintViolationException(final String message,
                                              final Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(message, constraintViolations);
    }

    /**
     * Creates a custom constraint violation report.
     *
     * @param constraintViolations A {@code Set} of {@link ConstraintViolation}s or null.
     */
    public CustomConstraintViolationException(final Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(constraintViolations);
    }
}
