package dev.practice.resttemplate;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class RestTemplateTest {

    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8001); // port 없으면 랜덤
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @DisplayName("500 error 로 리턴되면 HttpServerErrorException 가 발생한다.")
    @Test
    void error_500() {

        // given
        mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody("hello"));
        String url = mockWebServer.url("/test").toString();
        RestTemplate restTemplate = new RestTemplate();

        // when
        // then
        assertThatThrownBy(
                () -> restTemplate.getForObject(url, String.class)
        ).isInstanceOf(HttpServerErrorException.class)
                .hasMessage("500 Server Error: \"hello\"");
    }

    @DisplayName("400 error 로 리턴되면 HttpClientErrorException 가 발생한다.")
    @Test
    void error_400() {

        // given
        mockWebServer.enqueue(new MockResponse().setResponseCode(400).setBody("hello"));
        String url = mockWebServer.url("/test").toString();
        RestTemplate restTemplate = new RestTemplate();

        // when
        // then
        assertThatThrownBy(
                () -> restTemplate.getForObject(url, String.class)
        ).isInstanceOf(HttpClientErrorException.class)
                .hasMessage("400 Client Error: \"hello\"");
    }
}
