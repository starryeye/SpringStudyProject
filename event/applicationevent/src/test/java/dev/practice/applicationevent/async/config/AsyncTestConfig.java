package dev.practice.applicationevent.async.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@TestConfiguration
public class AsyncTestConfig {

    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() { // bean 이름은 applicationEventMulticaster 이어야 한다.

        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();

        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor()); // 모든 Spring Event 리스너는 SimpleAsyncTaskExecutor 스레드로 이벤트를 비동기로 처리한다.
        return eventMulticaster;
    }
}
