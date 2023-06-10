package dev.practice.OpenFeign.adapter.out.web;

import dev.practice.OpenFeign.application.port.out.CurrencyExchangeRate;
import dev.practice.OpenFeign.application.port.out.RequestExchangeRatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
class ExchangeRateWebAdapter implements RequestExchangeRatePort {

    private final MyFeignClient myFeignClient;

    @Override
    public CurrencyExchangeRate getLatestExchangeRate() {

        ResponseExchangeRates latest = myFeignClient.getLatest();

        return new CurrencyExchangeRate(
                latest.result(),
                latest.provider(),
                latest.documentation(),
                latest.termsOfUse(),
                latest.timeLastUpdateUnix(),
                latest.timeLastUpdateUtc(),
                latest.timeNextUpdateUnix(),
                latest.timeNextUpdateUtc(),
                latest.timeEolUnix(),
                latest.baseCode(),
                Collections.unmodifiableMap(latest.rates())
        );
    }

    /**
     * 아래 메서드는 총 2개의 스레드가 맞물려서 동작한다.
     * - 호출 스레드
     * - @Async 스레드
     *
     * 동작 과정
     * 호출 스레드: getLatestExchangeRateByAsync 메소드를 호출하는 스레드이다.
     * 이 스레드는 getLatestExchangeRateByAsync 메소드 호출을 시작하여 @Async 스레드로 메서드 실행을 위임한다.(비동기)
     * 그 결과로 반환되는 CompletableFuture 를 즉시 받는다.
     * 이후로 이 스레드는 다른 작업을 계속하거나, CompletableFuture 가 완료될 때까지 기다리거나, 콜백을 등록해 놓을 수도 있다.
     *
     * @Async 스레드: 메소드의 실행을 담당한다.
     * 이 스레드는 myFeignClient.getLatest2() 메소드를 호출하여 HTTP 요청을 시작한다.
     * myFeignClient.getLatest2() 메소드 호출을 하면 openFeign 을 사용하므로
     * 외부 API 요청/응답을 수행하는데 블로킹 방식으로 동작한다.
     * 응답이 오면 CompletableFuture.completedFuture 을 통해서 결과를 받아서
     * CompletableFuture 에 넣는다.
     *
     */
    @Async
    @Override
    public CompletableFuture<Map<String, Object>> getLatestExchangeRateByAsync() {
        /**
         * CompletableFuture.completedFuture() 메소드는
         * 이미 결과가 있는 상황에서 즉시 완료된 CompletableFuture 를 생성하는데 사용되는 메서드이다.
         * 또한, @Async 스레드로 openFeign 을 요청/응답을 동기, 블로킹 방식으로 처리하므로
         * CompletableFuture.completedFuture 은 적절한 선택이다.
         *
         * 만약 HTTP Client 내부적으로 비동기 작업이 처리된다면.. @Async 스레드는 블로킹 될 것이다.
         * 하지만, openfeign 은 비동기를 지원하지 않는다.
         *
         * myFeignClient.getLatest2() 의 리턴 형식은 Map<String, Object> 이다.
         */
        return CompletableFuture.completedFuture(myFeignClient.getLatest2());

        /**
         * 아래와 같이 써도 된다.. 대신 스레드 풀 관리는 어렵다.. 기본 스레드로 동작하니까..
         * CompletableFuture.supplyAsync 을 쓰면서 파라미터로 Executor 를 넣어주지 않으면
         * 기본 스레드로 동작한다. 따라서 @Async 가 필요 없다.
         * 또한, () -> myFeignClient.getLatest2() 는 당연히, 동기 블로킹 방식이다.
         * -> 응답이 올때 까지 CompletableFuture 가 관리하는 기본 스레드는 블로킹된다.
         */
//        return CompletableFuture.supplyAsync(() -> myFeignClient.getLatest2());
    }
}
