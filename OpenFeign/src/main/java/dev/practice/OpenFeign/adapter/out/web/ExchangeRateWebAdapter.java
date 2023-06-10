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
     * 아래 메서드는 총 3개의 스레드가 맞물려서 동작한다.
     * - 호출 스레드
     * - @Async 스레드
     * - Feign 내부 HTTP Client 의 네트워크 I/O 스레드
     *
     * 동작 과정
     * 호출 스레드: getLatestExchangeRateByAsync 메소드를 호출하는 스레드이다.
     * 이 스레드는 getLatestExchangeRateByAsync 메소드 호출을 시작하여 @Async 스레드로 메서드 실행을 위임한다.(비동기)
     * 그 결과로 반환되는 CompletableFuture 를 즉시 받는다.
     * 이후로 이 스레드는 다른 작업을 계속하거나, CompletableFuture 가 완료될 때까지 기다리거나, 콜백을 등록해 놓을 수도 있다.
     *
     * @Async 스레드: 메소드의 실행을 담당한다.
     * 이 스레드는 myFeignClient.getLatest2() 메소드를 호출하여 HTTP 요청을 시작합니다.
     * myFeignClient.getLatest2() 메소드 호출은 Feign 의 내부 I/O 스레드에 위임되므로,
     * @Async 스레드는 즉시 반환되고 스레드 풀에 반환됩니다.
     *
     * Feign 내장 HTTP Client I/O 스레드: Feign Client 의 내장 HTTP 클라이언트가 별도의 I/O 스레드에서 HTTP 요청을 수행한다.
     * 이 스레드는 HTTP 요청을 보내고, 응답을 받아서 처리합니다. (블로킹)
     * 응답이 도착하면, 이 스레드는 응답 데이터를 CompletableFuture 에 설정합니다.
     */
    @Async
    @Override
    public CompletableFuture<Map<String, Object>> getLatestExchangeRateByAsync() {
//        Map<String, Object> latest = myFeignClient.getLatest2();
//        return CompletableFuture.completedFuture(latest);
        /**
         * CompletableFuture.completedFuture() 메소드는
         * 이미 결과가 있는 상황에서 즉시 완료된 CompletableFuture 를 생성하는데 사용되므로,
         * 이 경우에는 적절하지 않다.
         * 이렇게 사용하면.. @Async 스레드가 스레드풀로 반환되지 않고 블로킹 될 것같다..
         */


        return myFeignClient.getLatest2();
    }
}
