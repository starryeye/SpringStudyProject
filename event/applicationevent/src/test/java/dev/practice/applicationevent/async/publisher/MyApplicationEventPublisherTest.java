package dev.practice.applicationevent.async.publisher;

import dev.practice.applicationevent.async.config.AsyncTestConfig;
import dev.practice.applicationevent.sync.publisher.MyApplicationEventPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import({AsyncTestConfig.class}) // ApplicationEventMulticaster 빈 생성
@SpringBootTest
class MyApplicationEventPublisherTest {

    @Autowired
    private MyApplicationEventPublisher publisher;

    /**
     * ApplicationEventMulticaster 를 새로 만들면서, TaskExecutor 를 넣어주면
     * 비동기로 이벤트 처리가 가능하다.
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