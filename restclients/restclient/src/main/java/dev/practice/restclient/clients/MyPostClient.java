package dev.practice.restclient.clients;

import dev.practice.restclient.clients.request.Post;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class MyPostClient {

    private final RestClient restClient;

    public MyPostClient(
            RestClient.Builder builder
//            ClientHttpRequestFactory jdkClientHttpRequestFactory
    ) {

        this.restClient = builder
                .baseUrl("https://jsonplaceholder.typicode.com")
//                .requestFactory(jdkClientHttpRequestFactory)
                .build();
    }

    public String findAll() {
        return restClient.get()
                .uri("/posts")
                .retrieve()
                .body(String.class);
    }

    public List<Post> getAll() {
        return restClient.get()
                .uri("/posts")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {})
                ;
    }
}
