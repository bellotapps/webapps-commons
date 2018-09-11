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

package com.bellotapps.webapps_commons.exceptions;

/**
 * {@link RuntimeException} thrown when there is any issue while interacting with external services.
 */
public class ExternalServiceException extends RuntimeException {

    /**
     * A human readable name of the service (i.e such as AWS S3).
     */
    private final String service;

    /**
     * Constructor which can set a {@code message}.
     *
     * @param service A human readable name of the service (i.e such as AWS S3).
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public ExternalServiceException(String service, String message) {
        super(message);
        this.service = service;
    }

    /**
     * Constructor which can set a {@code message} and a {@code cause}.
     *
     * @param service A human readable name of the service (i.e such as AWS S3).
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause   The cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                For more information, see {@link RuntimeException#RuntimeException(Throwable)}.
     */
    public ExternalServiceException(String service, String message, Throwable cause) {
        super(message, cause);
        this.service = service;
    }

    /**
     * @return A human readable name of the service (i.e such as AWS S3).
     */
    public String getService() {
        return service;
    }
}
