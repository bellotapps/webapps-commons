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
import com.bellotapps.webapps_commons.exceptions.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * {@link ExceptionHandler} in charge of handling {@link UnauthenticatedException}.
 * Will handle according to {@link HandlingResults#UNAUTHENTICATED} result.
 */
@ExceptionHandlerObject
public class UnauthenticatedExceptionHandler implements ExceptionHandler<UnauthenticatedException, Void> {

    /**
     * The {@link Logger} object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UnauthenticatedExceptionHandler.class);

    @Override
    public HandlingResult<Void> handle(final UnauthenticatedException exception) {
        LOGGER.debug("A user was not authenticated. UnauthenticatedException message: {}", exception.getMessage());
        LOGGER.trace("UnauthenticatedException Stack trace: ", exception);
        return HandlingResults.UNAUTHENTICATED;
    }
}
