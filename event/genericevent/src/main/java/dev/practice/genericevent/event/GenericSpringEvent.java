package dev.practice.genericevent.event;

import lombok.Builder;

public class GenericSpringEvent<T> {

    /**
     * the event class holds any content and a success status indicator
     */

    private final T what;
    protected final boolean success;

    @Builder
    private GenericSpringEvent(T what, boolean success) {
        this.what = what;
        this.success = success;
    }

    public T getWhat() {
        return what;
    }

    public boolean isSuccess() {
        return success;
    }
}
