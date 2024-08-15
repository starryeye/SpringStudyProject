package dev.practice.restclient.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.practice.restclient.clients.request.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(MyPostClient2.class)
class MyPostClientTest2 {

    @Autowired
    MyPostClient2 myPostClient2;

    /**
     * MyPostClient2 는 JdkClientHttpRequestFactory 전략으로 생성된 RestClient 를 사용한다.
     * MyPostClientTest 에서 설명한 것처럼 JdkClientHttpRequestFactory 로 생성된 RestClient 는 MockRestServiceServer 가 동작하지 않는다.
     *
     * 하지만, RestClientConfig2 와 같이 RestClientCustomizer 로 생성하면 MockRestServiceServer 정상 동작함..
     *
     * //todo 원래는 되어야하나.. 테스트 실패..
     */

    @Autowired
    MockRestServiceServer mockRestServiceServer; // Mock Server

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("모든 post 를 가져와야한다.")
    @Test
    void getAll() throws JsonProcessingException {

        // given
        List<Post> expectedPosts = List.of(
                new Post(1, 1, "Hello, world!", "This is Test Body"),
                new Post(2, 1, "Testing RestClient with @RestClientTest", "This is Test Body")
        );

        // stubbing
        mockRestServiceServer.expect(requestTo("https://jsonplaceholder.typicode.com/posts"))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(expectedPosts),
                        MediaType.APPLICATION_JSON
                ));

        // when
        List<Post> result = myPostClient2.getAll();

        // then
        assertThat(result).hasSize(2);
    }
}