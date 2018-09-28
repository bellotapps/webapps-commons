package com.bellotapps.webapps_commons.web.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data transfer object for transferring only one {@link Long} value with an API consumer.
 */
public class LongValueDto extends GenericValueDto<Long> {

    /**
     * @param value The value to be transferred.
     */
    @JsonCreator
    public LongValueDto(
            @JsonProperty(value = "value", access = JsonProperty.Access.WRITE_ONLY) final Long value) {
        super(value);
    }

    /**
     * @return The value to be transferred.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long getValue() {
        return super.getValue();
    }
}
