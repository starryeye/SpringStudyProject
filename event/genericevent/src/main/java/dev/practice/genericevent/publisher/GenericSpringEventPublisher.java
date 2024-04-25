package dev.practice.genericevent.publisher;

import dev.practice.genericevent.event.GenericSpringEvent;
import dev.practice.genericevent.event.GenericStringSpringEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenericSpringEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishGenericTypeParameterStringEvent(final String what, final boolean success) {

        log.info("Publishing GenericSpringEvent.. what : {}, tx : {}", what, Thread.currentThread().getName());

        /**
         * 주의 사항
         * GenericSpringEvent<String> 을 이벤트로 발행했는데..
         * listener 시그니처를 handleSuccessfulGenericSpringEvent(final GenericSpringEvent<T> event) 로 해야 동작한다.
         * handleSuccessfulGenericSpringEvent(final GenericSpringEvent<String> event) 으로 하면 동작하지 않는다.
         *
         * 해당 이벤트는
         * public <T> void handleSuccessfulGenericSpringEvent(final GenericSpringEvent<T> event) 에서 동작
         */
        GenericSpringEvent<String> event = new GenericSpringEvent<>(what, success);

        applicationEventPublisher.publishEvent(event);
    }

    public void publishStringEvent(final String what, final boolean success) {

        log.info("Publishing GenericStringSpringEvent.. what : {}, tx : {}", what, Thread.currentThread().getName());

        /**
         * 해당 이벤트는
         * public void handleSuccessfulGenericStringSpringEvent(final GenericStringSpringEvent event)
         * public <T> void handleSuccessfulGenericSpringEvent(final GenericSpringEvent<T> event)
         * 두군데서 모두 동작됨
         */
        GenericSpringEvent<String> event = new GenericStringSpringEvent(what, success); // 다형성 사용!!!

        applicationEventPublisher.publishEvent(event);
    }
}
