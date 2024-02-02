package com.example.restclients.restclient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UsingRestClient {

    private final RestClient myRestClient;

    public void run() {

        // WebClient 와 굉장히 유사하게 사용할 수 있는 동기, blocking 방식의 Client 이다.
        // Http Interface 와는 다르게.. WebFlux 의 의존성 없이 사용가능하다.


        Map<String, Map<String, Double>> response = myRestClient.get()
                .uri("/v6/latest")
                .retrieve()
                .body(Map.class);

        System.out.println("Using RestClient, KRW rates = " + response.get("rates").get("KRW"));
    }
}
