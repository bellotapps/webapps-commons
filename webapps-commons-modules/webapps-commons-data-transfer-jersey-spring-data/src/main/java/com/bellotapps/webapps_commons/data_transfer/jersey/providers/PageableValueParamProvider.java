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


import org.glassfish.jersey.internal.inject.InjectionManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;

/**
 * An extension of the {@link AbstractPagingValueParamProvider} for {@link Pageable}s.
 */
@Provider
public class PageableValueParamProvider
        extends AbstractPagingValueParamProvider<Pageable, Sort, Sort.Order, Sort.Direction> {
    /**
     * Constructor.
     *
     * @param injectionManager An {@link InjectionManager} used to inject data taken from query params.
     */
    @Inject
    public PageableValueParamProvider(final InjectionManager injectionManager) {
        super(Pageable.class,
                injectionManager,
                PageRequest::of,
                Sort.Direction::fromOptionalString,
                () -> Sort.DEFAULT_DIRECTION,
                (property, direction) -> Sort.Order.by(property).with(direction),
                Sort::by,
                PageRequest::of);
    }
}
