package com.example.restclients.httpinterface;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UsingHttpInterface {

    private final MyHttpInterfaceClient myHttpInterfaceClient;

    public void run() {

        // 요청 및 응답
        Map<String, Map<String, Double>> response = myHttpInterfaceClient.getLatest();


        System.out.println("Using HttpInterface, KRW rates = " + response.get("rates").get("KRW"));

        //사용하는 입장에서 보면 굉장히 직관적이다. Rest 를 쓰는지.. DB를 접근하는지.. 모르고 추상화가 잘 됨.

        //참고, Client 생성해주는 작업도 Spring Data JPA 처럼 어노테이션으로 추후 나올듯..하다.
    }
}
