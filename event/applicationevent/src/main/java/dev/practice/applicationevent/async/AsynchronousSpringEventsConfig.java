package dev.practice.applicationevent.async;

import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

//@Configuration
public class AsynchronousSpringEventsConfig {

    /**
     * 얘를 동작시키면, 기존에 sync 패키지에 있던 애들이 async 로 동작한다...
     * Test 에서만 켜주도록 한다. -> async.config.AsyncTestConfig
     */

//    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() { // bean 이름은 applicationEventMulticaster 이어야 한다.

        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();

        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor()); // 모든 Spring Event 리스너는 SimpleAsyncTaskExecutor 스레드로 이벤트를 비동기로 처리한다.
        return eventMulticaster;
    }
}
