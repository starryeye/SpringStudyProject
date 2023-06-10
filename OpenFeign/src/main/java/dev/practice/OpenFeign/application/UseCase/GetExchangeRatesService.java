package dev.practice.OpenFeign.application.UseCase;

import dev.practice.OpenFeign.application.port.in.GetExchangeRatesQuery;
import dev.practice.OpenFeign.application.port.out.RequestExchangeRatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
class GetExchangeRatesService implements GetExchangeRatesQuery {

    private final RequestExchangeRatePort requestExchangeRatePort;

    /**
     * - CompletableFuture 을.. 그냥 get() 하면 현재 스레드가 블로킹이 될 수 있다.
     * - thenAccept() 를 사용하여 람다식을 통해 콜백 함수를 등록하였고..
     * 콜백함수는 비동기 작업 스레드(@Async 스레드) 에 의해 수행된다. 그래서 블로킹을 피했다.
     * - exceptionally() 를 이용하여 비동기 작업 결과가 실패일 경우 비어있는 Map 을 리턴하는 것으로 안정성을 높혔다.
     */
    @Override
    public void getExchangeRate() {
        requestExchangeRatePort.getLatestExchangeRateByAsync()
                .exceptionally(ex -> {
                    System.out.println("Failed to get exchange rate: " + ex.getMessage());
                    return Collections.emptyMap();
                })
                .thenAccept(result -> {
                    Object ratesObject = result.get("rates");
                    if (ratesObject instanceof Map) {
                        Map<String, Double> rates = (Map<String, Double>) ratesObject;
                        Double krwExchangeRate = rates.get("KRW");
                        if (krwExchangeRate != null) {
                            System.out.println("KRW Exchange Rate : " + krwExchangeRate);
                        } else {
                            System.out.println("No KRW exchange rate available");
                        }
                    } else {
                        System.out.println("Rates is not a map");
                    }
                });
    }

}
