package dev.practice.restclient.clients;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class SimpleInterceptorClient {

    private final RestClient restClient;

    public SimpleInterceptorClient(
            RestClient.Builder restClientBuilder,
            ClientHttpRequestInterceptor myInterceptor
    ) {
        this.restClient = restClientBuilder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .requestFactory(new JdkClientHttpRequestFactory())
                .requestInterceptor(myInterceptor)
                .build();
    }

    public String getAllTodos() {
        return restClient.get()
                .uri("/todos")
                .retrieve()
                .body(String.class)
                ;
    }
}
