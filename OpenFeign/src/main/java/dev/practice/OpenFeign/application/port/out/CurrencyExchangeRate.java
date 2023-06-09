package dev.practice.OpenFeign.application.port.out;

import java.util.Map;

public record CurrencyExchangeRate(
        String result,
        String provider,
        String documentation,
        String terms_of_use,
        Long time_last_update_unix,
        String time_last_update_utc,
        Long time_next_update_unix,
        String time_next_update_utc,
        Long time_eol_unix,
        String base_code,
        Map<String, Double> rates
) {
}
