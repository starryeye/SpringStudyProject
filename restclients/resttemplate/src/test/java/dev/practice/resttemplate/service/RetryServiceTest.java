package dev.practice.resttemplate.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RetryServiceTest {

    @Autowired
    private RetryService retryService;

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

    @DisplayName("error code 4xx, 5xx 응답 시, retry 가 3 회 실행 되어야 한다.")
    @Test
    void retry() {

        // given
        // 500 error 리턴하여 retry 하도록
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        String url = mockWebServer.url("/test").toString();

        // when
        // then
        assertThatThrownBy(
                () -> retryService.execute(url)
        ).isInstanceOf(HttpServerErrorException.class)
                .hasMessage("500 Server Error: [no body]");

        assertThat(mockWebServer.getRequestCount()).isEqualTo(3); // retry 횟수 체크
    }
}