package com.example.restclients.httpinterface;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfig {

    @Bean
    public MyHttpInterfaceClient myHttpInterfaceClient(
            WebClient myWebClient
    ) {
        // HTTP Interface Client 생성

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(myWebClient)) // WebClient 로 생성할 수 있다.
                .build();

        return httpServiceProxyFactory.createClient(MyHttpInterfaceClient.class); // 대상 interface
    }
}
