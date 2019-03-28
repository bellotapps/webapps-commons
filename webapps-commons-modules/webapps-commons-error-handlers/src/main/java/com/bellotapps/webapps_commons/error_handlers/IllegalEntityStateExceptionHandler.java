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
import com.bellotapps.webapps_commons.constants.HttpStatus;
import com.bellotapps.webapps_commons.exceptions.IllegalEntityStateException;
import com.bellotapps.webapps_commons.web.dtos.api_errors.IllegalEntityStateErrorDto;

/**
 * {@link ExceptionHandler} in charge of handling {@link IllegalEntityStateException}.
 * Will result into a <b>422 Unprocessable Entity</b> response.
 */
@ExceptionHandlerObject
public class IllegalEntityStateExceptionHandler
        implements ExceptionHandler<IllegalEntityStateException, IllegalEntityStateErrorDto> {

    @Override
    public HandlingResult<IllegalEntityStateErrorDto> handle(final IllegalEntityStateException exception) {
        return HandlingResult.withPayload(HttpStatus.UNPROCESSABLE_ENTITY.getCode(),
                new IllegalEntityStateErrorDto(exception.getError()));
    }
}
