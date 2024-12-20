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
    /**
     * Spring 에서 생성해준 RestClient.Builder 를 주입 받는다.
     *
     * RestClientConfig 와는 관련이 없다. RestClientConfig 는 참고용임
     */

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
                .body(new ParameterizedTypeReference<List<Post>>() {})
                ;
    }
}
