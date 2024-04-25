package dev.practice.genericevent.publisher;

import dev.practice.genericevent.listener.AnnotationDrivenEventListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class GenericSpringEventPublisherTest {


    @Autowired
    private GenericSpringEventPublisher genericSpringEventPublisher;

    @Autowired
    private AnnotationDrivenEventListener annotationDrivenEventListener;


    @DisplayName("success 가 true 인 이벤트를 발행하면 해당 리스너가 비동기로 동작한다. @EventListener 의 컨디션을 보면 success 조건 있음")
    @Test
    void publishGenericTypeParameterStringEvent() {

        /**
         * GenericSpringEvent<String> event = new GenericSpringEvent<>(xxx); 를 발행하였고
         * listener 에서 GenericSpringEvent<T> 로 받아진다.
         */

        // given
        String message = "Hello, world";
        boolean success = true;
        assertThat(annotationDrivenEventListener.isHitSuccessfulEventHandler1()).isFalse();

        // when
        genericSpringEventPublisher.publishGenericTypeParameterStringEvent(message, success);

        // then
        try {
            Thread.sleep(200); // 비동기 시간 벌어 주기
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertThat(annotationDrivenEventListener.isHitSuccessfulEventHandler1()).isTrue();
    }

    @DisplayName("success 가 true 인 이벤트를 발행하면 해당 리스너가 비동기로 동작한다. @EventListener 의 컨디션을 보면 success 조건 있음")
    @Test
    void publishStringEvent() {

        /**
         * GenericSpringEvent<String> event = new GenericStringSpringEvent<>(xxx); 를 발행하였고 (다형성)
         * listener 에서 GenericStringSpringEvent, GenericSpringEvent<T> 로 받아진다.
         */

        // given
        String message = "Hello, world";
        boolean success = true;
        assertThat(annotationDrivenEventListener.isHitSuccessfulEventHandler2()).isFalse();

        // when
        genericSpringEventPublisher.publishStringEvent(message, success);

        // then
        try {
            Thread.sleep(200); // 비동기 시간 벌어 주기
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertThat(annotationDrivenEventListener.isHitSuccessfulEventHandler2()).isTrue();
    }
}