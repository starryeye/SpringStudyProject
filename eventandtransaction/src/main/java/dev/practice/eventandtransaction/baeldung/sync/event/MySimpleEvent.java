package dev.practice.eventandtransaction.baeldung.sync.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MySimpleEvent {

    private final String message;

    @Builder
    private MySimpleEvent(String message) {
        this.message = message;
    }
}
