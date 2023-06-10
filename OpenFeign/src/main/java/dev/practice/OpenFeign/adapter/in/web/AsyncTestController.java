package dev.practice.OpenFeign.adapter.in.web;

import dev.practice.OpenFeign.application.port.in.AsyncTestPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequiredArgsConstructor
public class AsyncTestController {

    private final AsyncTestPort asyncTestPort; //내부 모든 메서드는 5초간 대기한다.

    @GetMapping("/async-test")
    public String asyncTest() {

        /**
         * 호출 스레드에서 아래 메서드를 호출하면 즉시 반환(void) 되고..
         * 비동기로 작업 스레드에서 아래 메서드가 수행된다.
         */
        asyncTestPort.asyncMethod();

        return "Request finished"; //"Async task finished" 프린트 보다 먼저 수행 된다.
    }

    @GetMapping("/async-test/future")
    public String asyncTestWithFuture() throws ExecutionException, InterruptedException {

        /**
         * asyncMethodWithFuture() 메서드를 호출하여 즉시 Future 를 얻는다.
         * 그런 다음 다른 작업을 계속 수행한다(// Do other things...).
         * 비동기 작업이 완료될 때까지 기다릴 필요가 없으므로 이 시간 동안 다른 유용한 작업을 수행할 수 있다.
         *
         * 비동기 작업이 완료되면 future.isDone()이 true 를 반환하므로 future.get()을 호출하여 결과를 가져올 수 있다.
         * 이 결과는 이전에 비동기 작업에서 반환한 "Hello, World!" 문자열이다.
         * -> while(true) 로 isDone() 이 true 될때 까지 기다리지 않고
         * -> while 없이 바로 future.get() 을 호출하면.. 블로킹 상태로 반환될 때 까지 대기한다.
         */

        Future<String> future = asyncTestPort.asyncMethodWithFuture();

        // Do other things...

        while (true) {
            if (future.isDone()) {
                String result = future.get();
                // Use result...
                break;
            }
        }

        return "done";
    }

    @GetMapping("/async-test/completable-future")
    public String asyncTestWithCompletableFuture() {

        /**
         * CompletableFuture..
         *
         * Future는 결과를 받아오는 방법이 블로킹 방식이라는 한계가 있다.
         * 즉, Future.get() 메소드를 호출하면 결과가 준비될 때까지 호출 스레드가 블록되어 대기하게 된다.
         * 이는 비효율적일 수 있으며, 또한 콜백 방식으로 사용할 수 없다.
         *
         * 이런 문제를 해결하기 위해 Java 8에서는 CompletableFuture 라는 클래스를 도입했다.
         * CompletableFuture 는 Future 를 구현하면서 동시에 CompletionStage 인터페이스도 구현하므로,
         * 비동기 작업의 완료 알림 및 결과 처리에 대해 더 세밀한 제어를 가능하게 한다.
         *
         *
         * CompletableFuture 특징
         *
         * 1. CompletableFuture는 thenAccept 메서드를 제공하는데,
         * 이는 비동기 작업이 완료되었을 때 실행할 콜백을 등록하는데 사용된다.
         *
         * 2. thenAccept 메서드는 람다 식이나 메서드 참조를 받아, 비동기 작업이 완료되면 작업 스레드가 이를 실행한다.
         * 따라서 이 방법을 사용하면 비동기 작업의 결과를 즉시 처리할 수 있다.
         * -> 그래서 호출 스레드는 블로킹 없이 다른 작업을 계속 수행할 수 있는 것이다.
         *
         * 3. CompletableFuture 는 연결된 비동기 작업을 쉽게 생성할 수 있도록
         * thenApply, thenCompose, thenCombine 등의 메서드를 제공한다.
         * 이 메서드들은 각각 결과 변환, 비동기 작업 연결, 두 비동기 작업의 결과 결합 등의 용도로 사용된다.
         *
         * 4. CompletableFuture 는 supplyAsync 라는 메서드도 제공한다.
         * 2가지 큰 기능을 제공한다.
         * -> 비동기적으로 작업을 실행하는 역할이다.
         * -> 해당 비동기 작업이 끝나면 그 결과를 포함하는 CompletableFuture 객체를 반환한다.
         * 또한,supplyAsync 는 2가지 형태로 오버로드 되어있다.
         * -> CompletableFuture.supplyAsync(Supplier<U> supplier):
         * -> -> 여기서 Supplier<U>는 결과를 반환하는 함수형 인터페이스이다.
         * -> -> 이 메소드는 작업을 ForkJoinPool.commonPool()에서 비동기적으로 실행하고,
         * -> -> 그 결과를 가진 CompletableFuture를 반환한다.
         * -> CompletableFuture.supplyAsync(Supplier<U> supplier, Executor executor):
         * -> -> 이 메소드는 첫 번째 메소드와 동일하지만, 두 번째 인자로 Executor를 받아서,
         * -> -> 해당 Executor에서 작업을 비동기적으로 실행하도록 한다.
         *
         *
         * 결국, completableFuture 를 사용하면.. 콜백을 사용할 수 있는 것이다.
         *
         * 아래 메서드의 로직은 다음과 같다.
         *
         * 호출 스레드는 CompletableFuture 를 즉시 반환받는다.
         * 작업 스레드는 5초 후 String 을 CompletableFuture 로 밀어 넣어준다.
         * thenAccept() 메서드로 콜백 함수(아래에선 프린트)가 등록 되어있으므로 작업 스레드는 콜백 함수를 실행한다.
         * CompletableFuture 에 있던 String 값은 파라미터로 사용한다.
         */
        asyncTestPort.asyncMethodWithCompletableFuture()
                .thenAccept(result -> System.out.println(result));

        return "Request finished"; //역시.. thenAccept() 은 5초 뒤 수행되므로 thenAccept() 보다 응답을 내려주는게 더 빠르다.
    }
}
