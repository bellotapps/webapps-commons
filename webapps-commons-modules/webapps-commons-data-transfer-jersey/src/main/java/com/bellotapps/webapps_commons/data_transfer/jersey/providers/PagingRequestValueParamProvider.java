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


import com.bellotapps.webapps_commons.persistence.repository_utils.PagingRequest;
import com.bellotapps.webapps_commons.persistence.repository_utils.SortingData;
import com.bellotapps.webapps_commons.persistence.repository_utils.SortingData.PropertySort;
import com.bellotapps.webapps_commons.persistence.repository_utils.SortingData.SortDirection;
import org.glassfish.jersey.internal.inject.InjectionManager;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;

/**
 * An extension of the {@link AbstractPagingValueParamProvider} for {@link PagingRequest}s.
 */
@Provider
public class PagingRequestValueParamProvider
        extends AbstractPagingValueParamProvider<PagingRequest, SortingData, PropertySort, SortDirection> {
    /**
     * Constructor.
     *
     * @param injectionManager An {@link InjectionManager} used to inject data taken from query params.
     */
    @Inject
    public PagingRequestValueParamProvider(
            final InjectionManager injectionManager) {
        super(PagingRequest.class,
                injectionManager,
                PagingRequest::unsorted,
                SortDirection::fromStringOptional,
                SortDirection::defaultDirection,
                (property, direction) -> PropertySort.builder().by(property).withDirection(direction).build(),
                list -> {
                    final var builder = SortingData.builder();
                    list.forEach(builder::sortBy);
                    return builder.build();
                },
                PagingRequest::of);
    }
}
