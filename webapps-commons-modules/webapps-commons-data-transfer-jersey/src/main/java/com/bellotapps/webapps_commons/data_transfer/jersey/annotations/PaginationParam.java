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

package com.bellotapps.webapps_commons.data_transfer.jersey.annotations;

import java.lang.annotation.*;

/**
 * Indicates that a parameter contains data for creating an object with paging and sorting information.
 */
@Target({ElementType.PARAMETER,})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PaginationParam {

    /**
     * Indicates the size of a page when this value is not provided.
     *
     * @return The default page size to be used.
     */
    int defaultPageSize() default 25;

    /**
     * Indicates the page number when this value is not provided.
     *
     * @return The default page number to be used.
     */
    int defaultPageNumber() default 0;

    /**
     * Indicates the maximum page size that is accepted for the page.
     *
     * @return The max. page size for the page.
     */
    int maxPageSize() default 100;
}
