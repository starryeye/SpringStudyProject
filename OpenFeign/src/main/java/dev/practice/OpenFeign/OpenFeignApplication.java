package dev.practice.OpenFeign;

import dev.practice.OpenFeign.application.port.out.CurrencyExchangeRate;
import dev.practice.OpenFeign.application.port.out.RequestExchangeRatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@RequiredArgsConstructor
@SpringBootApplication
public class OpenFeignApplication {

	private final RequestExchangeRatePort requestExchangeRatePort;

	public static void main(String[] args) {
		SpringApplication.run(OpenFeignApplication.class, args);
	}

	/**
	 * Spring Cloud OpenFeign 을 이용한 동기 방식 요청
	 */
	@Bean
	ApplicationRunner runner() {
		return args -> {
			CurrencyExchangeRate rate = requestExchangeRatePort.getLatestExchangeRate();

			System.out.println("KRW Exchage Rate: " + rate.rates().get("KRW"));
			System.out.println(rate);
		};
	}

}
