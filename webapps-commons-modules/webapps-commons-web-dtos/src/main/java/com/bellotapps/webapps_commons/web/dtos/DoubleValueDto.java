package com.bellotapps.webapps_commons.web.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data transfer object for transferring only one {@link Double} value with an API consumer.
 */
public class DoubleValueDto extends GenericValueDto<Double> {

    /**
     * @param value The value to be transferred.
     */
    @JsonCreator
    public DoubleValueDto(
            @JsonProperty(value = "value", access = JsonProperty.Access.WRITE_ONLY) final Double value) {
        super(value);
    }

    /**
     * @return The value to be transferred.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Double getValue() {
        return super.getValue();
    }
}
