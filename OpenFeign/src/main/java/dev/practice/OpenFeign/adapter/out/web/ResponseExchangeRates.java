package dev.practice.OpenFeign.adapter.out.web;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
record ResponseExchangeRates(
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
