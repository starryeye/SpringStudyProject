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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(MyPostClient.class)
class MyPostClientTest {

    @Autowired
    MyPostClient myPostClient;

    /**
     * 참고
     * 현재 테스트는 requestFactory 를 설정하지 않은 RestClient 로 테스트 중이다.
     * RestClient 를 jdkClientHttpRequestFactory 를 사용할 경우..
     * MockRestServiceServer 의 Stubbing 이 동작하지 않는 듯 하다..
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
        List<Post> result = myPostClient.getAll();

        // then
        assertThat(result).hasSize(2);
    }
}