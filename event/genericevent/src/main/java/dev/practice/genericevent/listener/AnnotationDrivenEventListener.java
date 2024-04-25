package dev.practice.genericevent.listener;

import dev.practice.genericevent.event.GenericSpringEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnnotationDrivenEventListener {

    // for test
    private boolean hitSuccessfulEventHandler = false;

    @Async(value = "threadPoolTaskExecutor") // async 로 동작하도록 한다. 없으면 sync 로 동작
    @EventListener(condition = "#event.success") // SpEL
    public <T> void handleSuccessful(final GenericSpringEvent<T> event) { // todo, GenericSpringEvent<String> 으로 하면 왜 동작 안함?

        log.info("Received GenericSpringEvent<T>.. what : {}, tx : {}", event.getWhat(), Thread.currentThread().getName());
        hitSuccessfulEventHandler = true;
    }

    public boolean isHitSuccessfulEventHandler() {
        return hitSuccessfulEventHandler;
    }
}
