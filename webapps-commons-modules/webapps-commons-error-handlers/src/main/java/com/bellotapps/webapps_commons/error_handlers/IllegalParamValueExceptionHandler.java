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

package com.bellotapps.webapps_commons.error_handlers;


import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;
import com.bellotapps.utils.error_handler.HandlingResult;
import com.bellotapps.webapps_commons.exceptions.IllegalParamValueException;
import com.bellotapps.webapps_commons.web.dtos.api_errors.IllegalParamValueErrorDto;

import java.util.List;


/**
 * {@link ExceptionHandler} in charge of handling {@link IllegalParamValueException}.
 * Will handle according to {@link HandlingResults#illegalParams(List)} result.
 */
@ExceptionHandlerObject
public class IllegalParamValueExceptionHandler
        implements ExceptionHandler<IllegalParamValueException, IllegalParamValueErrorDto> {

    @Override
    public HandlingResult<IllegalParamValueErrorDto> handle(final IllegalParamValueException exception) {
        return HandlingResults.illegalParams(exception.getConflictingParams());
    }
}
