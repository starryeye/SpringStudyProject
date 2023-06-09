package dev.practice.OpenFeign.application.port.out;

public interface RequestExchangeRatePort {

    CurrencyExchangeRate getLatestExchangeRate();
}
