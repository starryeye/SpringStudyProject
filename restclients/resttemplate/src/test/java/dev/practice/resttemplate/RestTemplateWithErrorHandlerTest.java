package dev.practice.resttemplate;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("read-2-connection-2-timeout")
@SpringBootTest
public class RestTemplateWithErrorHandlerTest {

    @Autowired
    private RestTemplate restTemplateWithErrorHandler;

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

    @DisplayName("5xx error 를 받으면, MyResponseErrorHandler 에 의해 커스텀된 동작이 실행된다.")
    @Test
    void error_5xx() {

        // given
        mockWebServer.enqueue(new MockResponse().setResponseCode(502).setBody("server error!"));
        String url = mockWebServer.url("/test").toString();

        // when
        // then
        assertThatThrownBy(
                () -> restTemplateWithErrorHandler.getForObject(url, String.class)
        ).isInstanceOf(HttpServerErrorException.class)
                .hasMessage("502 Server error, status code : 502 BAD_GATEWAY, response body : server error!");
    }

    @DisplayName("4xx error 를 받으면, MyResponseErrorHandler 에 의해 커스텀된 동작이 실행된다.")
    @Test
    void error_4xx() {

        // given
        mockWebServer.enqueue(new MockResponse().setResponseCode(407).setBody("client error!"));
        String url = mockWebServer.url("/test").toString();

        // when
        // then
        assertThatThrownBy(
                () -> restTemplateWithErrorHandler.getForObject(url, String.class)
        ).isInstanceOf(HttpClientErrorException.class)
                .hasMessage("407 Client error, status code : 407 PROXY_AUTHENTICATION_REQUIRED, response body : client error!");
    }

    @DisplayName("ResourceAccessException 은 ResponseErrorHandler 로 처리가 불가능하다. restTemplate 호출 라인에서 try-catch 로 잡아줘야한다.")
    @Test
    void connectionTimeout() {

        // given
        String nonExistingUrl = "http://10.255.255.1:12345/test"; // 존재하지 않는 호스트

        // when
        // then
        assertThatThrownBy(
                () -> restTemplateWithErrorHandler.getForObject(nonExistingUrl, String.class)
        ).isInstanceOf(ResourceAccessException.class)
                .hasMessage("I/O error on GET request for \"http://10.255.255.1:12345/test\": Connect timed out");
    }
}
