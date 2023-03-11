package com.example.restclients;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Map;

@SpringBootApplication
public class RestclientsApplication {

	//Application 실행 후, 수
	@Bean
	ApplicationRunner init(ErApi api) {
		return args -> {
			// USD 기준의 다른 통화와의 환율 정보 GET API
			// https://open.er-api.com/v6/latest

			//REST Clients 3가지
			//https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#spring-integration


			//1. RestTemplate
			// 동시성 이슈 없음. 쓰레드간 공유 가능
			RestTemplate rt = new RestTemplate();

//			String res = rt.getForObject("https://open.er-api.com/v6/latest", String.class);
//			System.out.println("res = " + res);

//			Map<String, Object> res = rt.getForObject("https://open.er-api.com/v6/latest", Map.class);
//			System.out.println(res.get("rates"));

			Map<String, Map<String, Double>> res = rt.getForObject("https://open.er-api.com/v6/latest", Map.class);
			System.out.println(res.get("rates").get("KRW"));


			//2. WebClient
			WebClient client = WebClient.create("https://open.er-api.com");
			Map<String, Map<String, Double>> res2 = client.get().uri("/v6/latest").retrieve().bodyToMono(Map.class).block();
			System.out.println(res2.get("rates").get("KRW"));
			//retrieve() : 실제 exchange 가 실행된다. 서버에 요청보냄
			//bodyToMono(Map.class) : 응답의 body 를 mono 로 바꿈
			//block() : 현재의 스레드를 block 시킨 상태로.. 요청하겠다.
			//WebClient 는 뒤에서 별도의 스레드로 동작한다. 기본이 Netty Client 가 동작됨.
			//-> WebClient 는 Netty Client 의 worker thread 에서 동작한다고 볼 수 있음
			//-> block() 은 Netty Client 의 worker thread 에 비동기적으로 요청해놓고 현재의 스레드는 단순 block 상태로 대기하다가 응답을 받아오는 형식이다.


			//3-1. HTTP Interface, 내부에서 생성해서 사용 (Spring 6 에서 신규 추가)
			HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
					.builder(WebClientAdapter.forClient(client))
					.build();
			//내부적으로 WebClient 를 사용하기 때문에 client 를 넘겨줘야한다.

			ErApi erApi = httpServiceProxyFactory.createClient(ErApi.class);
			Map<String, Map<String, Double>> res3 = erApi.getLatest();
			System.out.println(res3.get("rates").get("KRW"));
			// 정의한 Interface 를 바탕으로 Rest Client 구현체를 만들어 준다. 이후, 바로 사용

			//3-2. Http Interface, 외부 Bean DI 방식
			Map<String, Map<String, Double>> res4 = api.getLatest();
			System.out.println(res3.get("rates").get("KRW"));
			//사용하는 입장에서 보면 굉장히 직관적이다. Rest 를 쓰는지.. DB를 접근하는지.. 모르고 추상화가 잘 됨.

			//참고, Client 생성해주는 작업도 Spring Data JPA 처럼 어노테이션으로 추후 나올듯..하다.
		};
	}

	@Bean
	ErApi erApi() {
		WebClient client = WebClient.create("https://open.er-api.com");
		HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
				.builder(WebClientAdapter.forClient(client))
				.build();
		return httpServiceProxyFactory.createClient(ErApi.class);
	}

	interface ErApi {
		@GetExchange("/v6/latest")
		Map getLatest();
	}

	public static void main(String[] args) {
		SpringApplication.run(RestclientsApplication.class, args);
	}

}
