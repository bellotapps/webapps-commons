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

package com.bellotapps.webapps_commons.error_handlers;

import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;
import com.bellotapps.utils.error_handler.HandlingResult;
import com.bellotapps.webapps_commons.web.dtos.api_errors.ServerErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

/**
 * {@link ExceptionHandler} in charge of handling {@link DataAccessException}.
 * Will handle according to {@link HandlingResults#SERVICE_UNAVAILABLE} result.
 */
@ExceptionHandlerObject
public class DataAccessExceptionHandler implements ExceptionHandler<DataAccessException, ServerErrorDto> {

    /**
     * The {@link Logger} object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataAccessExceptionHandler.class);

    @Override
    public HandlingResult<ServerErrorDto> handle(DataAccessException exception) {
        LOGGER.error("Could not access database", exception);
        return HandlingResults.SERVICE_UNAVAILABLE;
    }
}
