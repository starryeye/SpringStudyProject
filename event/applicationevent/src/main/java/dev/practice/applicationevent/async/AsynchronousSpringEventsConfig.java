package dev.practice.applicationevent.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import static org.springframework.context.support.AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME;

//@Configuration
public class AsynchronousSpringEventsConfig {

    /**
     * 얘를 동작시키면, 기존에 sync 패키지에 있던 애들이 async 로 동작한다...
     * Test 에서만 켜주도록 한다. -> async.config.AsyncTestConfig
     */

//    @Bean(name = APPLICATION_EVENT_MULTICASTER_BEAN_NAME)
    public ApplicationEventMulticaster applicationEventMulticaster() { // bean 이름은 applicationEventMulticaster 이어야 한다. 혹은 여기서와 같이 APPLICATION_EVENT_MULTICASTER_BEAN_NAME 를 사용해준다.

        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();

//        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor()); // 모든 Spring Event 리스너는 SimpleAsyncTaskExecutor 스레드로 이벤트를 비동기로 처리한다.
        eventMulticaster.setTaskExecutor(asyncExecutor()); // 모든 Spring Event 리스너는 asyncExecutor() 반환 스레드로 이벤트를 비동기로 처리한다.
        return eventMulticaster;
    }

    private Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(10000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(10);
        executor.initialize();
        return executor;
    }
}
