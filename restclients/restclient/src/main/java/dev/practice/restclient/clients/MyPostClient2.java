package dev.practice.restclient.clients;

import dev.practice.restclient.clients.request.Post;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class MyPostClient2 {
    /**
     * RestClientConfig2 에서 생성된 RestClient 빈을 주입받는다.
     * JdkClientHttpRequestFactory 전략을 사용하였다.
     */

    private final RestClient restClient;

    public MyPostClient2(RestClient restClient) {
        this.restClient = restClient;
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
