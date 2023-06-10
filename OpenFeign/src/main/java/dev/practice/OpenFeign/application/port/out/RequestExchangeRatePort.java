package dev.practice.OpenFeign.application.port.out;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface RequestExchangeRatePort {

    CurrencyExchangeRate getLatestExchangeRate();

    CompletableFuture<Map<String, Object>> getLatestExchangeRateByAsync();
}
