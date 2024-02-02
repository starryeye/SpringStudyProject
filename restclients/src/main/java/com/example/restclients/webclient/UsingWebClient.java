package com.example.restclients.webclient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UsingWebClient {

    private final WebClient myWebClient;

    public void run() {

        //2. WebClient
        Map<String, Map<String, Double>> response = myWebClient.get()
                .uri("/v6/latest")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        System.out.println("Using WebClient, KRW rates = " + response.get("rates").get("KRW"));

        //retrieve() : 실제 exchange 가 실행된다. 서버에 요청보냄
        //bodyToMono(Map.class) : 응답의 body 를 mono 로 바꿈
        //block() : 현재의 스레드를 block 시킨 상태로.. 요청하겠다.
        //WebClient 는 뒤에서 별도의 스레드로 동작한다. 기본이 Netty Client 가 동작됨.
        //-> WebClient 는 Netty Client 의 worker thread 에서 동작한다고 볼 수 있음
        //-> block() 은 Netty Client 의 worker thread 에 비동기적으로 요청해놓고 현재의 스레드는 단순 block 상태로 대기하다가 응답을 받아오는 형식이다.
    }
}
