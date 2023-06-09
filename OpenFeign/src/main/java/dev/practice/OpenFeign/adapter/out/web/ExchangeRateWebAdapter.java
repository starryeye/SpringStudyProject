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
                latest.terms_of_use(),
                latest.time_last_update_unix(),
                latest.time_last_update_utc(),
                latest.time_next_update_unix(),
                latest.time_next_update_utc(),
                latest.time_eol_unix(),
                latest.base_code(),
                Collections.unmodifiableMap(latest.rates())
        );
    }
}
