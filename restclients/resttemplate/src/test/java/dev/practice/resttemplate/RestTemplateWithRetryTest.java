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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("3-retry")
@SpringBootTest
public class RestTemplateWithRetryTest {

    @Autowired
    private RestTemplate restTemplateWithRetry;

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

    /**
     * 현재 restTemplate 에 적용된 interceptor 는 error code 4xx, 5xx 응답 시 retry 를 하지 못한다.
     *
     * 쉬운 방법인.. @Retryable 을 이용하여 error code 4xx, 5xx 응답 시 retry 하도록 해보자
     * -> RetryServiceTest.java
     */

    @DisplayName("ResourceAccessException 발생 시, retry 가 3 회 실행 되어야 한다.")
    @Test
    void retry_ResourceAccessException() {

        // given
        String nonExistingUrl = "http://10.255.255.1:12345/test"; // 존재하지 않는 호스트

        // when
        // then
        assertThatThrownBy(
                () -> restTemplateWithRetry.getForObject(nonExistingUrl, String.class)
        ).isInstanceOf(ResourceAccessException.class)
                .hasMessage("I/O error on GET request for \"http://10.255.255.1:12345/test\": Connect timed out");
    }
}
