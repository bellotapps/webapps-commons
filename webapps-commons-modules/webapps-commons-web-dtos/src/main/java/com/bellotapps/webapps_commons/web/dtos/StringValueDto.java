package com.bellotapps.webapps_commons.web.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data transfer object for transferring only one {@link String} value with an API consumer.
 */
public class StringValueDto extends GenericValueDto<String> {

    /**
     * @param value The value to be transferred.
     */
    @JsonCreator
    public StringValueDto(
            @JsonProperty(value = "value", access = JsonProperty.Access.WRITE_ONLY) final String value) {
        super(value);
    }

    /**
     * @return The value to be transferred.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getValue() {
        return super.getValue();
    }
}
