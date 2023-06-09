package dev.practice.OpenFeign.adapter.out.web;

import dev.practice.OpenFeign.application.port.out.CurrencyExchangeRate;
import dev.practice.OpenFeign.application.port.out.RequestExchangeRatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;

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
}
