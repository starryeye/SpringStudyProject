package dev.practice.genericevent.listener;

import dev.practice.genericevent.event.GenericSpringEvent;
import dev.practice.genericevent.event.GenericStringSpringEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnnotationDrivenEventListener {

    /**
     * 리스너 메서드 파라미터를 GenericSpringEvent<String> 으로 하면 동작 안하는 이유의 힌트이다.
     * The event publisher..
     * But due to type erasure,
     * we need to publish an event that resolves the generics parameter we would filter on,
     * for example, class GenericStringSpringEvent extends GenericSpringEvent<String>.
     * -> GenericStringSpringEvent.java 처럼 상속 받고 다형성을 이용해서 publisher 에서 발행..
     *
     * 참고> 이벤트 체이닝..
     * Also, there’s an alternative way of publishing events.
     * If we return a non-null value from a method annotated with @EventListener as the result,
     * Spring Framework will send that result as a new event for us.
     * Moreover, we can publish multiple new events by returning them
     * in a collection as the result of event processing.
     */


    // for test
    private boolean hitSuccessfulEventHandler1 = false;
    private boolean hitSuccessfulEventHandler2 = false;

    @Async(value = "threadPoolTaskExecutor") // async 로 동작하도록 한다. 없으면 sync 로 동작
    @EventListener(condition = "#event.success") // SpEL
    public <T> void handleSuccessfulGenericSpringEvent(final GenericSpringEvent<T> event) { // GenericSpringEvent<String> 으로 수신하면 동작 안함

        log.info("Received GenericSpringEvent<T>.. what : {}, tx : {}", event.getWhat(), Thread.currentThread().getName());
        hitSuccessfulEventHandler1 = true;
    }

    @Async(value = "threadPoolTaskExecutor")
    @EventListener(condition = "#event.success") // SpEL
    public void handleSuccessfulGenericStringSpringEvent(final GenericStringSpringEvent event) { // Generic"String"SpringEvent

        log.info("Received GenericStringSpringEvent<T>.. what : {}, tx : {}", event.getWhat(), Thread.currentThread().getName());
        hitSuccessfulEventHandler2 = true;
    }

    public boolean isHitSuccessfulEventHandler1() {
        return hitSuccessfulEventHandler1;
    }
    public boolean isHitSuccessfulEventHandler2() {
        return hitSuccessfulEventHandler2;
    }
}
