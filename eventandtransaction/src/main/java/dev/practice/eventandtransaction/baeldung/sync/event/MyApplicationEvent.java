package dev.practice.eventandtransaction.baeldung.sync.event;

import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MyApplicationEvent extends ApplicationEvent {

    private final String message;

    @Builder
    private MyApplicationEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
}
