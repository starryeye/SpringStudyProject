package dev.practice.OpenFeign.application.port.out;

import java.util.Map;

public record CurrencyExchangeRate(
        String result,
        String provider,
        String documentation,
        String termsOfUse,
        Long timeLastUpdateUnix,
        String timeLastUpdateUtc,
        Long timeNextUpdateUnix,
        String timeNextUpdateUtc,
        Long timeEolUnix,
        String baseCode,
        Map<String, Double> rates
) {
}
