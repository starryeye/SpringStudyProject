package dev.practice.applicationevent.sync.publisher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyApplicationEventPublisherTest {

    @Autowired
    private MyApplicationEventPublisher publisher;

    /**
     * Spring framework 의 ApplicationContext 는
     * 기본적으로 동기식인 사용자 정의 이벤트를 발행하고 리스닝할 수 있는 기능을 제공한다.
     * -> 동기식 이므로 하나의 트랜잭션에 쉽게 묶을 수 있다.
     */

    @DisplayName("ApplicationEvent 를 상속하지 않는 이벤트로 publish, listen 이 동작해야한다.")
    @Test
    void myElasticApplicationListener() {

        // given
        String eventMessage = "Hello, world";

        // when
        publisher.publishMySimpleEvent(eventMessage);

        // then
        // "Received MySimpleEvent.. message: {}, tx : {}" 로그 확인
    }

    @DisplayName("ApplicationEvent 를 상속하는 이벤트로 publish, listen 이 동작해야한다.")
    @Test
    void myApplicationListener() {

        // given
        String eventMessage = "Hello, world";

        // when
        publisher.publishMyApplicationEvent(eventMessage);

        // then
        // "Received MyApplicationEvent.. message: {}, tx : {}" 로그 확인
    }
}