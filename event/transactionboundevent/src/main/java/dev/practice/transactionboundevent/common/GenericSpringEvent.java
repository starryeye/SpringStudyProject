package dev.practice.transactionboundevent.common;

import lombok.Getter;

@Getter
public class GenericSpringEvent<T> {

    /**
     * the event class holds any content and a success status indicator
     */

    private final T what;
    protected final boolean isSuccess;

    public GenericSpringEvent(T what, boolean isSuccess) {
        this.what = what;
        this.isSuccess = isSuccess;
    }
}
