package com.bellotapps.webapps_commons.web.dtos;

/**
 * Data transfer object for transferring only one object of type {@code <T>} value with an API consumer.
 *
 * @param <T> The concrete type of object to be transferred.
 */
public abstract class GenericValueDto<T> {

    /**
     * The value to be transferred.
     */
    private final T value;

    /**
     * Constructor.
     *
     * @param value The value to be transferred.
     */
    protected GenericValueDto(final T value) {
        this.value = value;
    }

    /**
     * @return The value to be transferred.
     */
    public T getValue() {
        return value;
    }
}
