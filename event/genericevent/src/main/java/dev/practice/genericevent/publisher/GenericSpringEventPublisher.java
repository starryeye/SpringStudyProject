package dev.practice.genericevent.publisher;

import dev.practice.genericevent.event.GenericSpringEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenericSpringEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(final String what, final boolean success) {

        log.info("Publishing GenericSpringEvent.. what : {}, tx : {}", what, Thread.currentThread().getName());

        GenericSpringEvent<String> event = GenericSpringEvent.<String>builder()
                .what(what)
                .success(success)
                .build();

        applicationEventPublisher.publishEvent(event);
    }
}
