package com.example.restclients;

import com.example.restclients.httpinterface.UsingHttpInterface;
import com.example.restclients.restclient.UsingRestClient;
import com.example.restclients.resttemplate.UsingRestTemplate;
import com.example.restclients.webclient.UsingWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class RestclientsApplication {

	private final UsingRestTemplate usingRestTemplate;
	private final UsingWebClient usingWebClient;
	private final UsingHttpInterface usingHttpInterface;
	private final UsingRestClient usingRestClient;

	@Bean
	ApplicationRunner runner() {
		return args -> {
			// USD 기준의 다른 통화와의 환율 정보 GET API
			// https://open.er-api.com/v6/latest

			//Spring 6 에서 사용할 수 있는.. REST Clients 4가지 (Spring boot 3)
			//https://docs.spring.io/spring-framework/reference/integration/rest-clients.html

			// RestClient 는 Spring boot 3.2 부터 추가됨.

			usingRestTemplate.run(); // RestTemplate
			usingWebClient.run(); // WebClient
			usingHttpInterface.run(); // Http Interface
			usingRestClient.run(); // RestClient
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(RestclientsApplication.class, args);
	}

}
