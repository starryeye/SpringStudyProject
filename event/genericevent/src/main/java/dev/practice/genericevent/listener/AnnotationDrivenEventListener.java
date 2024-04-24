package dev.practice.genericevent.listener;

import dev.practice.genericevent.event.GenericSpringEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnnotationDrivenEventListener {

    @Async // async 로 동작하도록 한다. 없으면 sync 로 동작
    @EventListener(condition = "#event.success") // SpEL
    public void handleSuccessful(GenericSpringEvent<String> event) {

        log.info("Received GenericSpringEvent<String>.. ");
    }
}
