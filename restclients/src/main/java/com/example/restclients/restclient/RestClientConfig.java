package com.example.restclients.restclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient myRestClient() {

//        return RestClient.create("https://open.er-api.com");

        return RestClient.builder()
//                .requestFactory(new HttpComponentsClientHttpRequestFactory())
//                .messageConverters(converters -> converters.add(new MyCustomMessageConverter()))
                .baseUrl("https://open.er-api.com")
//                .defaultUriVariables(Map.of("variable", "foo"))
//                .defaultHeader("My-Header", "Foo")
//                .requestInterceptor(myCustomInterceptor)
//                .requestInitializer(myCustomInitializer)
                .build();
    }
}
