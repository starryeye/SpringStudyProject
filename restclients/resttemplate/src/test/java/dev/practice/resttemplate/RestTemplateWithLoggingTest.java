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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("3-retry")
@SpringBootTest
public class RestTemplateWithLoggingTest {

    @Autowired
    private RestTemplate restTemplateWithLogging;

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

    @DisplayName("logging 이 잘 찍히는지 보자")
    @Test
    void logging() {

        // given
        // 500 error 리턴하여 retry 하도록
        mockWebServer.enqueue(new MockResponse().setBody("hello"));
        mockWebServer.enqueue(new MockResponse().setBody("world"));
        mockWebServer.enqueue(new MockResponse().setBody("logging"));
        String url = mockWebServer.url("/test").toString();

        // when
        // then
        String result1 = restTemplateWithLogging.getForObject(url, String.class);
        String result2 = restTemplateWithLogging.getForObject(url, String.class);
        String result3 = restTemplateWithLogging.getForObject(url, String.class);

        assertThat(result1).isEqualTo("hello");
        assertThat(result2).isEqualTo("world");
        assertThat(result3).isEqualTo("logging");

        assertThat(mockWebServer.getRequestCount()).isEqualTo(3); // retry 횟수 체크
    }
}
