package dev.practice.OpenFeign;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {

    /**
     * @Async 어노테이션이 메서드에 적용되면, 해당 메서드는 별도의 스레드에서 실행된다.
     * 별도의 스레드는 이 메서드처럼 개발자가 설정한 스레드 풀에서 관리가 된다.
     *
     * 스프링에서 @Async 어노테이션을 지원하기 위해 내부적으로 TaskExecutor 인터페이스를 사용한다.
     * TaskExecutor 는 Java 의 Executor 인터페이스를 기반으로 스프링에서 확장한 것이다.
     *
     * @Async 를 사용할 때 별도로 TaskExecutor 를 설정하지 않으면,
     * 스프링은 기본적으로 SimpleAsyncTaskExecutor 를 사용합니다.
     * 그러나 이 기본 TaskExecutor 는 매 요청마다 새로운 스레드를 생성하므로 실제 운영 환경에서는 사용에 주의가 필요하다.
     *
     * 따라서, 지금 이 클래스와 같이 AsyncConfigurer 를 구현하고,
     * getAsyncExecutor 메서드를 오버라이딩 하여 쓰레드 풀을 생성해주면
     * @Async 가 적용된 메서드가 호출될 때마다 새로운 스레드를 생성하지 않고 생성해놓은 스레드 풀에서 꺼내 쓴다.
     *
     * 따라서 일반적으로는 스프링의 @EnableAsync 어노테이션과 함께 AsyncConfigurer 를 구현하여 적절한 TaskExecutor 를 설정한다.
     */
    @Override
    public Executor getAsyncExecutor() {
        //TTL 설정도 있다.
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    /**
     * 추가로 공부한 내용.
     *
     * 톰캣 워커 스레드: 클라이언트로부터 웹 요청을 받아 처리하고, 컨트롤러의 메서드를 실행하는 역할을 담당한다.
     * @Async 어노테이션이 붙은 메서드를 호출하면, 이 메서드의 처리를 비동기 작업 스레드에게 위임하고,
     * 자신은 다른 웹 요청을 처리하거나 풀에 반환되어 대기한다.
     *
     * 비동기 작업 스레드: @Async 어노테이션이 붙은 메서드의 비즈니스 로직을 처리하는 역할을 담당한다.
     * - 외부 시스템에 비동기 요청을 보내는 작업을 수행한다면(webClient), 이 요청의 처리를 별도의 I/O 스레드에게 위임한다.
     * - 외부 시스템에 동기 요청을 보내는 작업을 수행한다면(openFeign), 해당 스레드에서 처리한다.ㅅ
     *
     * 별도의 I/O 스레드: 비동기 HTTP 클라이언트 라이브러리가 내부적으로 관리하는 스레드로,
     * 네트워크 I/O 작업을 비동기적으로 처리하는 역할을 담당한다.
     * 외부 시스템에 비동기 요청을 보내고, 이에 대한 응답을 대기하고 받아 처리하는 작업을 수행한다.
     * webflux 에 해당한다. (webClient)
     *
     *
     * 스레드 풀 주의사항
     *
     * 톰캣 워커 스레드: 워커 스레드 풀의 크기는 일반적으로 서버의 CPU 코어 수에 비례하게 설정한다.
     * -> 워커 스레드가 주로 CPU 바운드 작업(예: 비즈니스 로직 처리)을 수행하기 때문.
     *
     * 비동기 작업 스레드: @Async 작업은 주로 I/O 바운드 작업(예: 네트워크 요청, DB 쿼리 등)을 처리하므로,
     * 비동기 작업 스레드 풀의 크기는 서버의 CPU 코어 수보다 훨씬 크게 설정할 수 있다.
     * 하지만 너무 많은 스레드는 메모리와 CPU 스위칭 오버헤드를 증가시키므로 적절한 균형을 찾는 것이 중요
     *
     * 별도의 I/O 스레드:
     * 별도의 I/O 스레드는 대개 네트워크 I/O 작업에 최적화된 이벤트 기반 모델을 사용하므로, 실제 스레드 수는 그다지 많지 않아도 된다.
     * webflux 에 해당한다. (webClient)
     */

    @Bean(name = "customExecutor")
    public Executor customExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Custom-Async-");
        executor.initialize();
        return executor;
    }
}
