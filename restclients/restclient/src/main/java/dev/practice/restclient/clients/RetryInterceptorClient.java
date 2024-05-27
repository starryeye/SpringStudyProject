package dev.practice.restclient.clients;

import am.ik.spring.http.client.RetryableClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.web.client.RestClient;

import java.util.Set;

@Component
public class RetryInterceptorClient {

    private final RestClient restClient;

    public RetryInterceptorClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .requestInterceptor(
                        new RetryableClientHttpRequestInterceptor( // https://github.com/making/retryable-client-http-request-interceptor
                                new FixedBackOff(100, 2),
                                Set.of(500, 503)
                        )
                )
                .baseUrl("https://jsonplaceholder.typicode.com")
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
