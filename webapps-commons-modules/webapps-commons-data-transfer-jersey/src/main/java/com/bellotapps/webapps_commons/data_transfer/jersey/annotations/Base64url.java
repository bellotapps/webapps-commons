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

package com.bellotapps.webapps_commons.data_transfer.jersey.annotations;

import java.lang.annotation.*;

/**
 * Indicates that a parameter is encoded using Url-Safe base64 mechanism (i.e RFC 4648, section 5).
 *
 * @see <a href="https://tools.ietf.org/html/rfc4648#section-5">RFC 4648, section 5 </a>
 */
@Target({ElementType.PARAMETER,})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Base64url {
}
