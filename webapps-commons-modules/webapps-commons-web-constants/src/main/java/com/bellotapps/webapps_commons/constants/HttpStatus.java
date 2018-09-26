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

package com.bellotapps.webapps_commons.constants;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enum containing HTTP status codes.
 * <p>
 * The purpose of this enum is to define all statuses without depending on external libraries.
 *
 * @see <a href="http://www.iana.org/assignments/http-status-codes">HTTP Status Code Registry</a>
 * @see <a href="http://en.wikipedia.org/wiki/List_of_HTTP_status_codes">List of HTTP status codes - Wikipedia</a>
 */
public enum HttpStatus {

    // ========================================
    // 1xx Informational
    // ========================================

    /**
     * {@code 100 Continue}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.2.1">
     * HTTP/1.1: Semantics and Content, section 6.2.1</a>
     */
    CONTINUE(100, "Continue"),
    /**
     * {@code 101 Switching Protocols}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.2.2">
     * HTTP/1.1: Semantics and Content, section 6.2.2</a>
     */
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),
    /**
     * {@code 102 Processing}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2518#section-10.1">WebDAV</a>
     */
    PROCESSING(102, "Processing"),
    /**
     * {@code 103 Checkpoint}.
     *
     * @see <a href="http://code.google.com/p/gears/wiki/ResumableHttpRequestsProposal">A proposal for supporting
     * resumable POST/PUT HTTP requests in HTTP/1.0</a>
     */
    CHECKPOINT(103, "Checkpoint"),


    // ========================================
    // 2xx Successful
    // ========================================

    /**
     * {@code 200 OK}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.3.1">
     * HTTP/1.1: Semantics and Content, section 6.3.1</a>
     */
    OK(200, "OK"),
    /**
     * {@code 201 Created}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.3.2">
     * HTTP/1.1: Semantics and Content, section 6.3.2</a>
     */
    CREATED(201, "Created"),
    /**
     * {@code 202 Accepted}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.3.3">
     * HTTP/1.1: Semantics and Content, section 6.3.3</a>
     */
    ACCEPTED(202, "Accepted"),
    /**
     * {@code 203 Non-Authoritative Information}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.3.4">
     * HTTP/1.1: Semantics and Content, section 6.3.4</a>
     */
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
    /**
     * {@code 204 No Content}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.3.5">
     * HTTP/1.1: Semantics and Content, section 6.3.5</a>
     */
    NO_CONTENT(204, "No Content"),
    /**
     * {@code 205 Reset Content}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.3.6">
     * HTTP/1.1: Semantics and Content, section 6.3.6</a>
     */
    RESET_CONTENT(205, "Reset Content"),
    /**
     * {@code 206 Partial Content}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7233#section-4.1">HTTP/1.1: Range Requests, section 4.1</a>
     */
    PARTIAL_CONTENT(206, "Partial Content"),
    /**
     * {@code 207 Multi-Status}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-13">WebDAV</a>
     */
    MULTI_STATUS(207, "Multi-Status"),
    /**
     * {@code 208 Already Reported}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc5842#section-7.1">WebDAV Binding Extensions</a>
     */
    ALREADY_REPORTED(208, "Already Reported"),
    /**
     * {@code 226 IM Used}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc3229#section-10.4.1">Delta encoding in HTTP</a>
     */
    IM_USED(226, "IM Used"),


    // ========================================
    // 3xx Redirection
    // ========================================

    /**
     * {@code 300 Multiple Choices}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.4.1">
     * HTTP/1.1: Semantics and Content, section 6.4.1</a>
     */
    MULTIPLE_CHOICES(300, "Multiple Choices"),
    /**
     * {@code 301 Moved Permanently}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.4.2">
     * HTTP/1.1: Semantics and Content, section 6.4.2</a>
     */
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    /**
     * {@code 302 Found}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.4.3">
     * HTTP/1.1: Semantics and Content, section 6.4.3</a>
     */
    FOUND(302, "Found"),
    /**
     * {@code 303 See Other}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.4.4">
     * HTTP/1.1: Semantics and Content, section 6.4.4</a>
     */
    SEE_OTHER(303, "See Other"),
    /**
     * {@code 304 Not Modified}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7232#section-4.1">HTTP/1.1: Conditional Requests, section 4.1</a>
     */
    NOT_MODIFIED(304, "Not Modified"),
    /**
     * {@code 307 Temporary Redirect}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.4.7">
     * HTTP/1.1: Semantics and Content, section 6.4.7</a>
     */
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),
    /**
     * {@code 308 Permanent Redirect}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7238">RFC 7238</a>
     */
    PERMANENT_REDIRECT(308, "Permanent Redirect"),


    // ========================================
    // 4xx Client error
    // ========================================

    /**
     * {@code 400 Bad Request}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.1">
     * HTTP/1.1: Semantics and Content, section 6.5.1</a>
     */
    BAD_REQUEST(400, "Bad Request"),
    /**
     * {@code 401 Unauthorized}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7235#section-3.1">
     * HTTP/1.1: Authentication, section 3.1</a>
     */
    UNAUTHORIZED(401, "Unauthorized"),
    /**
     * {@code 402 Payment Required}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.2">
     * HTTP/1.1: Semantics and Content, section 6.5.2</a>
     */
    PAYMENT_REQUIRED(402, "Payment Required"),
    /**
     * {@code 403 Forbidden}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.3">
     * HTTP/1.1: Semantics and Content, section 6.5.3</a>
     */
    FORBIDDEN(403, "Forbidden"),
    /**
     * {@code 404 Not Found}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.4">
     * HTTP/1.1: Semantics and Content, section 6.5.4</a>
     */
    NOT_FOUND(404, "Not Found"),
    /**
     * {@code 405 Method Not Allowed}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.5">
     * HTTP/1.1: Semantics and Content, section 6.5.5</a>
     */
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    /**
     * {@code 406 Not Acceptable}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.6">
     * HTTP/1.1: Semantics and Content, section 6.5.6</a>
     */
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    /**
     * {@code 407 Proxy Authentication Required}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7235#section-3.2">HTTP/1.1: Authentication, section 3.2</a>
     */
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    /**
     * {@code 408 Request Timeout}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.7">
     * HTTP/1.1: Semantics and Content, section 6.5.7</a>
     */
    REQUEST_TIMEOUT(408, "Request Timeout"),
    /**
     * {@code 409 Conflict}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.8">
     * HTTP/1.1: Semantics and Content, section 6.5.8</a>
     */
    CONFLICT(409, "Conflict"),
    /**
     * {@code 410 Gone}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.9">
     * HTTP/1.1: Semantics and Content, section 6.5.9</a>
     */
    GONE(410, "Gone"),
    /**
     * {@code 411 Length Required}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.10">
     * HTTP/1.1: Semantics and Content, section 6.5.10</a>
     */
    LENGTH_REQUIRED(411, "Length Required"),
    /**
     * {@code 412 Precondition failed}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7232#section-4.2">
     * HTTP/1.1: Conditional Requests, section 4.2</a>
     */
    PRECONDITION_FAILED(412, "Precondition Failed"),
    /**
     * {@code 413 Payload Too Large}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.11">
     * HTTP/1.1: Semantics and Content, section 6.5.11</a>
     * @since 4.1
     */
    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
    /**
     * {@code 414 URI Too Long}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.12">
     * HTTP/1.1: Semantics and Content, section 6.5.12</a>
     * @since 4.1
     */
    URI_TOO_LONG(414, "URI Too Long"),
    /**
     * {@code 415 Unsupported Media Type}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.13">
     * HTTP/1.1: Semantics and Content, section 6.5.13</a>
     */
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    /**
     * {@code 416 Requested Range Not Satisfiable}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7233#section-4.4">HTTP/1.1: Range Requests, section 4.4</a>
     */
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested range not satisfiable"),
    /**
     * {@code 417 Expectation Failed}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.14">
     * HTTP/1.1: Semantics and Content, section 6.5.14</a>
     */
    EXPECTATION_FAILED(417, "Expectation Failed"),
    /**
     * {@code 418 I'm a teapot}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2324#section-2.3.2">HTCPCP/1.0</a>
     */
    I_AM_A_TEAPOT(418, "I'm a teapot"),
    /**
     * {@code 418 I'm a teapot}.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7540#section-9.1.2">HTTP/2, section 9.1.2</a>
     */
    MISDIRECTED_REQUEST(421, "Misdirected Request"),
    /**
     * {@code 422 Unprocessable Entity}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-11.2">WebDAV</a>
     */
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
    /**
     * {@code 423 Locked}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-11.3">WebDAV</a>
     */
    LOCKED(423, "Locked"),
    /**
     * {@code 424 Failed Dependency}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-11.4">WebDAV</a>
     */
    FAILED_DEPENDENCY(424, "Failed Dependency"),
    /**
     * {@code 426 Upgrade Required}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2817#section-6">Upgrading to TLS Within HTTP/1.1</a>
     */
    UPGRADE_REQUIRED(426, "Upgrade Required"),
    /**
     * {@code 428 Precondition Required}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc6585#section-3">Additional HTTP Status Codes</a>
     */
    PRECONDITION_REQUIRED(428, "Precondition Required"),
    /**
     * {@code 429 Too Many Requests}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc6585#section-4">Additional HTTP Status Codes</a>
     */
    TOO_MANY_REQUESTS(429, "Too Many Requests"),
    /**
     * {@code 431 Request Header Fields Too Large}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc6585#section-5">Additional HTTP Status Codes</a>
     */
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
    /**
     * {@code 451 Unavailable For Legal Reasons}.
     *
     * @see <a href="https://tools.ietf.org/html/draft-ietf-httpbis-legally-restricted-status-04">
     * An HTTP Status Code to Report Legal Obstacles</a>
     * @since 4.3
     */
    UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"),


    // ========================================
    // 5xx Server error
    // ========================================

    /**
     * {@code 500 Internal Server Error}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.6.1">HTTP/1.1: Semantics and Content, section 6.6.1</a>
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    /**
     * {@code 501 Not Implemented}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.6.2">HTTP/1.1: Semantics and Content, section 6.6.2</a>
     */
    NOT_IMPLEMENTED(501, "Not Implemented"),
    /**
     * {@code 502 Bad Gateway}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.6.3">HTTP/1.1: Semantics and Content, section 6.6.3</a>
     */
    BAD_GATEWAY(502, "Bad Gateway"),
    /**
     * {@code 503 Service Unavailable}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.6.4">HTTP/1.1: Semantics and Content, section 6.6.4</a>
     */
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    /**
     * {@code 504 Gateway Timeout}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.6.5">HTTP/1.1: Semantics and Content, section 6.6.5</a>
     */
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),
    /**
     * {@code 505 HTTP Version Not Supported}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.6.6">HTTP/1.1: Semantics and Content, section 6.6.6</a>
     */
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported"),
    /**
     * {@code 506 Variant Also Negotiates}
     *
     * @see <a href="http://tools.ietf.org/html/rfc2295#section-8.1">Transparent Content Negotiation</a>
     */
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),
    /**
     * {@code 507 Insufficient Storage}
     *
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-11.5">WebDAV</a>
     */
    INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
    /**
     * {@code 508 Loop Detected}
     *
     * @see <a href="http://tools.ietf.org/html/rfc5842#section-7.2">WebDAV Binding Extensions</a>
     */
    LOOP_DETECTED(508, "Loop Detected"),
    /**
     * {@code 509 Bandwidth Limit Exceeded}
     */
    BANDWIDTH_LIMIT_EXCEEDED(509, "Bandwidth Limit Exceeded"),
    /**
     * {@code 510 Not Extended}
     *
     * @see <a href="http://tools.ietf.org/html/rfc2774#section-7">HTTP Extension Framework</a>
     */
    NOT_EXTENDED(510, "Not Extended"),
    /**
     * {@code 511 Network Authentication Required}.
     *
     * @see <a href="http://tools.ietf.org/html/rfc6585#section-6">Additional HTTP Status Codes</a>
     */
    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");


    /**
     * The status code.
     */
    private final int code;

    /**
     * The {@link Family} corresponding to the status code.
     */
    private final Family family;

    /**
     * The reason phrase for the status code.
     */
    private final String reasonPhrase;


    /**
     * Constructor.
     *
     * @param code         The status code.
     * @param reasonPhrase The reason phrase for the status code.
     */
    HttpStatus(final int code, final String reasonPhrase) {
        Assert.isTrue(code >= 100 && code < 600,
                "Status codes must be between 100 (inclusive) and 600 (exclusive)");
        Assert.hasText(reasonPhrase, "The reason phrase should be present");
        this.code = code;
        this.family = Family.ofStatusCode(code);
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * @return The status code.
     */
    public int getCode() {
        return code;
    }

    /**
     * @return The {@link Family} corresponding to the status code.
     */
    public Family getFamily() {
        return family;
    }

    /**
     * @return The reason phrase for the status code.
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    /**
     * @return Whether this {@link HttpStatus} is {@link Family#INFORMATIONAL}.
     */
    public boolean isInformational() {
        return isOfFamily(Family.INFORMATIONAL);
    }

    /**
     * @return Whether this {@link HttpStatus} is {@link Family#SUCCESSFUL}.
     */
    public boolean isSuccessful() {
        return isOfFamily(Family.SUCCESSFUL);
    }

    /**
     * @return Whether this {@link HttpStatus} is {@link Family#REDIRECTION}.
     */
    public boolean isRedirection() {
        return isOfFamily(Family.REDIRECTION);
    }

    /**
     * @return Whether this {@link HttpStatus} is {@link Family#CLIENT_ERROR}.
     */
    public boolean isClientError() {
        return isOfFamily(Family.CLIENT_ERROR);
    }

    /**
     * @return Whether this {@link HttpStatus} is {@link Family#SERVER_ERROR}.
     */
    public boolean isServerError() {
        return isOfFamily(Family.SERVER_ERROR);
    }

    /**
     * Indicates whether this {@link HttpStatus} is of the given {@link Family}.
     *
     * @param family The {@link Family} to check against with.
     * @return {@code true} if this {@link HttpStatus} is of the given {@link Family}, or {@code false} otherwise.
     */
    private boolean isOfFamily(final Family family) {
        Assert.notNull(family, "A family must be given in order to check against it.");
        return family.equals(this.family);
    }


    // ============================================================================================================
    // Static stuff
    // ============================================================================================================

    /**
     * A {@link Map} holding, for each family code, the corresponding {@link Family} value.
     */
    private static final Map<Integer, HttpStatus> BY_CODE = Arrays.stream(values())
            .collect(Collectors.toMap(HttpStatus::getCode, Function.identity()));

    /**
     * Finds the corresponding {@link Family} for the given HTTP status code.
     *
     * @param statusCode The status code.
     * @return The corresponding {@link Family} for the given code.
     * @throws IllegalArgumentException If this enum does not define the given {@code statusCode}.
     */
    public static HttpStatus ofCode(final int statusCode) throws IllegalArgumentException {
        return Optional.ofNullable(BY_CODE.get(statusCode))
                .orElseThrow(() -> new IllegalArgumentException("No status code for value " + statusCode));
    }

    /**
     * Same as {@link #ofCode(int)}.
     *
     * @param statusCode The status code.
     * @return The corresponding {@link Family} for the given code.
     * @throws IllegalArgumentException If this enum does not define the given {@code statusCode}.
     */
    public static HttpStatus valueOf(final int statusCode) throws IllegalArgumentException {
        return ofCode(statusCode);
    }

    /**
     * Enum containing status families.
     */
    public enum Family {
        /**
         * Family for 1xx statuses.
         */
        INFORMATIONAL(1),
        /**
         * Family for 2xx statuses.
         */
        SUCCESSFUL(2),
        /**
         * Family for 3xx statuses.
         */
        REDIRECTION(3),
        /**
         * Family for 4xx statuses.
         */
        CLIENT_ERROR(4),
        /**
         * Family for 5xx statuses.
         */
        SERVER_ERROR(5);

        /**
         * First digit of the family.
         */
        private final int code;

        /**
         * Constructor.
         *
         * @param code First digit of the family.
         */
        Family(final int code) {
            Assert.isTrue(code >= 1 && code <= 5, "The family code must be between 1 and 5");
            this.code = code;
        }

        /**
         * @return The family code (i.e first digit of the family's statuses).
         */
        public int getCode() {
            return code;
        }

        // ============================================================================================================
        // Static stuff
        // ============================================================================================================

        /**
         * A {@link Map} holding, for each family code, the corresponding {@link Family} value.
         */
        private static final Map<Integer, Family> BY_FAMILY_CODE = Arrays.stream(values())
                .collect(Collectors.toMap(Family::getCode, Function.identity()));

        /**
         * Finds the corresponding {@link Family} for the given HTTP status code.
         *
         * @param statusCode The status code.
         * @return The corresponding {@link Family} for the given code.
         * @throws IllegalArgumentException If the given {@code statusCode} does not have a family defined
         *                                  (i.e is lower than 100 or greater than 599).
         */
        public static Family ofStatusCode(final int statusCode) throws IllegalArgumentException {
            final var familyCode = statusCode / 100;
            Assert.isTrue(familyCode >= 1 && familyCode <= 5, "No Family for statusCode " + statusCode);
            return Optional.ofNullable(BY_FAMILY_CODE.get(familyCode))
                    .orElseThrow(() -> new RuntimeException("This should not happen."));
        }
    }
}
