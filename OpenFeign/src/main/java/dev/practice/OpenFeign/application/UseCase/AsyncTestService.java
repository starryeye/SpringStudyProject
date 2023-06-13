package dev.practice.OpenFeign.application.UseCase;

import dev.practice.OpenFeign.application.port.in.AsyncTestPort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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

    /**
     * @Async 어노테이션의 원리
     *
     * @Async 어노테이션을 붙인 메소드는 Spring이 자동으로 비동기적으로 실행할 수 있다.
     * Spring 은 먼저 메소드를 호출하는 데 사용된 인터페이스나 클래스에 대한 프록시를 생성한다.
     * 이 프록시는 원본 메소드 호출을 가로채고, 그 메소드 호출을 TaskExecutor 인스턴스에 위임한다.
     * TaskExecutor 는 해당 작업을 별도의 스레드에서 실행한다.
     * -> 결국, AOP 이다.
     *
     * 따라서 @Async 어노테이션이 붙은 메소드를 호출하면, 메소드 호출은 즉시 반환되고,
     * 실제 작업은 백그라운드에서 별도의 스레드에서 실행된다.
     * 이렇게 하면 무거운 작업을 수행하는 동안 메인 스레드를 차단하지 않고 다른 작업을 계속 처리할 수 있다.
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
         * asyncMethodWithCompletableFuture 메서드는 총 3개의 스레드가 맞물려 동작하게 된다.
         * - asyncMethodWithCompletableFuture 를 호출한 '호출 스레드'
         * - @Async 에 의해 TaskExecutor 의 '@Async 스레드'
         * - CompletableFuture.supplyAsync 에 전달된 Executor 에 의한 'supplyAsync 스레드'
         *
         * 동작 과정은 다음과 같다.
         * 1. 호출 스레드가 asyncMethodWithCompletableFuture 메서드를 호출
         * 2. 호출 스레드는 메서드 실행을 @Async 스레드에 위임하고 즉시 CompletableFuture 을 가지고 반환된다. (비동기)
         * -> 호출 스레드 역할 끝, CompletableFuture 는 결과를 처리하기 위해서 쓰레드간 공유되는 객체라 보자..
         * 3. @Async 스레드는 CompletableFuture.supplyAsync 메서드를 호출
         * 4. @Async 스레드는 메서드 실행을 supplyAsync 스레드에 위임한다.
         * -> @Async 스레드 역할 끝
         * 5. supplyAsync 스레드는 5초간 스레드를 재우고, 문자열을 반환한다.
         * 6. supplyAsync 스레드가 작업이 완료되면 반환된 값은 CompletableFuture 에 넣는다.
         * -> supplyAsync 스레드 역할 끝
         *
         * 사실..
         * 이 메서드에서 @Async 는 필요없다.
         * -> 호출 스레드와 supplyAsync 두개로 이미 비동기 동작이다.
         * 또한, 특별한 스레드 설정을 하지 않고 있기 때문에..
         * Executor executor = Executors.newSingleThreadExecutor(); 이 코드도 필요없다.
         * -> 그냥 "기본 스레드" 를 사용하는 것으로 하면 된다.
         */
        Executor executor = Executors.newSingleThreadExecutor();

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }

            return "Async task finished";
        }, executor);
    }
    /**
     * asyncMethodWithCompletableFuture 메서드에 대한 추가 설명을 하겠다..
     *
     * 현재 asyncMethodWithCompletableFuture 메서드는 @Async 어노테이션이 적용되어있다.
     * 이는 별도의 스레드 풀에서 스레드를 할당하여 비동기로 처리하겠다는 의미이다.
     *
     * 주의>
     * @Async 어노테이션에 의한 비동기 처리 외에도
     * Completable.supplyAcync 에 의한 비동기 처리도 함께 이루어지고 있다.
     * -> @Async 에 의한 비동기는 TaskExecutor, CompletableFuture 에 의한 비동기는 CompletableFuture 가 관리하는 스레드
     *
     * 참고>
     * CompletableFuture.supplyAsync 메서드는 2개의 메서드로 오버로딩 되어있다.
     *
     * 첫번째..
     * 아래와 같이 직접 스레드 풀을 만들고 그 Executor 를 인자로 전달하여
     * 만든 스레드 풀로 비동기 작업을 수행하는 케이스가 있다.
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
     */
}
