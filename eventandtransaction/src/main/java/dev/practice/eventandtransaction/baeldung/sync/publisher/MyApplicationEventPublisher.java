package dev.practice.eventandtransaction.baeldung.sync.publisher;

import dev.practice.eventandtransaction.baeldung.sync.event.MyApplicationEvent;
import dev.practice.eventandtransaction.baeldung.sync.event.MySimpleEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyApplicationEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishMySimpleEvent(final String message) {

        log.info("Publishing MySimpleEvent.. message : {}, tx : {}", message, Thread.currentThread().getName());

        MySimpleEvent event = MySimpleEvent.builder()
                .message(message)
                .build();

        applicationEventPublisher.publishEvent(event); // event publishing
    }

    public void publishMyApplicationEvent(final String message) {

        log.info("Publishing MyApplicationEvent.. message : {}, tx : {}", message, Thread.currentThread().getName());

        MyApplicationEvent applicationEvent = MyApplicationEvent.builder()
                .source(this)
                .message(message)
                .build();

        applicationEventPublisher.publishEvent(applicationEvent);
    }
}
