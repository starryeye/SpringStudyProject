package dev.practice.OpenFeign.application.UseCase;

import dev.practice.OpenFeign.application.port.in.AsyncTestPort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
class AsyncTestService implements AsyncTestPort {

    /**
     * @Async 어노테이션은 Spring Framework에서 제공하는 기능이다.
     * 이 어노테이션은 메서드를 비동기로 실행하도록 한다
     * 이를 위해 일반적으로는 Future, CompletableFuture, ListenableFuture 등의 타입을 반환해야 한다.
     * -> 호출 스레드
     * -> -> @Async 어노테이션이 적용된 메서드를 호출한 호출 스레드는 메서드를 호출하고..
     * -> -> 일반적인 반환 값인 Future, CompletableFuture, ListenableFuture 등의 타입을 즉시 반환 받고 다음 로직을 수행한다.
     * -> 작업 스레드
     * -> -> @Async 어노테이션이 적용된 메서드는 작업 스레드가 새로 생기거나
     * -> -> 작업 스레드 풀에서 작업 스레드 하나가 할당되어 해당 메서드 로직을 수행한다.
     *
     * 그런데 만약 @Async가 붙은 메서드가 Future가 아닌 일반 클래스를 반환한다면,
     * 그 메서드는 여전히 비동기적으로 실행된다.
     * 하지만 비동기 작업의 결과를 받을 수 없게 된다.
     * 즉, 메서드의 호출자는 메서드가 반환될 때 그 결과를 기다리지 않고 바로 다음 작업으로 넘어간다.
     * 따라서 메서드의 실행 결과가 중요하다면 Future 등의 타입을 반환하도록 설계하는 것이 좋다.
     *
     * 또한 반환값이 void인 메서드에 @Async를 사용할 수도 있다.
     * 이런 경우에도 메서드는 비동기적으로 실행되지만, 결과를 반환하지 않는다.
     *
     */

    @Async //비동기로 작업 스레드에서 동작
    @Override
    public void asyncMethod() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        System.out.println("Async task finished");
    }

    @Async //비동기로 작업 스레드에서 동작
    @Override
    public Future<String> asyncMethodWithFuture() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        //Spring 6.0 부터 Deprecated 되었다. CompletableFuture 를 사용 권장.
        return new AsyncResult<String>("Hello, World!");
    }

    @Async //비동기로 작업 스레드에서 동작
    @Override
    public CompletableFuture<String> asyncMethodWithCompletableFuture() {

        /**
         * supplyAsync() 메서드는
         * 작업 스레드에서 수행할 작업을 Supplier 로 제공하면(여기선 람다),
         * 작업을 비동기적으로 실행하고 결과를 CompletableFuture 로 반환한다.
         */
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }

            return "Async task finished";
        });
    }
    /**
     * asyncMethodWithCompletableFuture 메서드에 대한 추가 설명을 하겠다..
     *
     * 현재 asyncMethodWithCompletableFuture 메서드는 @Async 어노테이션이 적용되어있다.
     * 이는 별도의 스레드 풀에서 스레드를 할당하여 비동기로 처리하겠다는 의미인데..
     * @Async 어노테이션을 빼도 비동기 처리가 된다.
     * -> CompletableFuture 는 비동기 작업의 결과를 처리하는 기능도 있지만, 그 자체로 비동기를 수행하는 기능도 있다.
     * 하지만, 할당되는 스레드 풀에 차이가 있다.
     *
     * @Async 어노테이션을 빼면 두가지 케이스로 나뉜다.
     *
     * 첫번째..
     * 아래와 같이 직접 스레드 풀을 만들어서 비동기 작업을 수행하는 케이스가 있다.
     * 참고로 아래 로직은 최대 10개의 스레드가 동시 작업을 가능케하는 스레드 풀을 만들고
     * 해당 스레드 풀에서 하나의 스레드가 할당되어 그 스레드에서 한번의 비동기 작업을 수행한다.
     * Executor executor = Executors.newFixedThreadPool(10);
     * CompletableFuture.supplyAsync(() -> {
     *     // 비동기 작업
     * }, executor);
     *
     * 두번째..
     * 위의 코드에서 supplyAsync 메소드에 Executor 인자를 전달하지 않는다면,
     * CompletableFuture 는 기본적으로 ForkJoinPool.commonPool() 을 사용하여 비동기 작업을 수행한다.
     * ForkJoinPool.commonPool() 은 JVM 당 하나만 존재하는 스레드 풀로 "기본 스레드"라 불린다.
     * 이렇게 되면 스레드 풀에 대한 세밀한 제어가 불가능해진 것이다.
     *
     * 따라서..
     * @Async 어노테이션을 사용하면서,
     * 스프링의 TaskExecutor 인터페이스를 구현한 Executor 빈을 통해 별도의 스레드 풀 설정을 하여..
     * 스레드 풀의 관리와 성능 튜닝에 대한 더욱 세밀한 제어를 할 수 있도록 하자.
     *
     * 참고>
     * @Async 도 적용하고 supplyAsync 메소드에 Executor 인자를 전달하면,
     * @Async 가 무시되고 전달한 Executor 로 비동기 작업이 수행된다.
     *
     * 참고>
     * @Async 어노테이션을 사용하면서,
     * 스프링의 TaskExecutor 인터페이스를 구현한 Executor 빈을 통해 별도의 스레드 풀 설정
     * 방식을 따르면..
     * TTL 설정을 못한다. 그래서, 첫번째 방법을 사용해야한다.
     */
}
