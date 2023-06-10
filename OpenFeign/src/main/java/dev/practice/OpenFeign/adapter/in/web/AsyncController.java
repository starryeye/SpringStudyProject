package dev.practice.OpenFeign.adapter.in.web;

import dev.practice.OpenFeign.application.port.in.GetExchangeRatesQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AsyncController {

    private final GetExchangeRatesQuery getExchangeRatesQuery;

    @GetMapping("/exchange-rate/async")
    public String getExchangeRates() {
        getExchangeRatesQuery.getExchangeRate();

        return "Request finished";
    }
}
