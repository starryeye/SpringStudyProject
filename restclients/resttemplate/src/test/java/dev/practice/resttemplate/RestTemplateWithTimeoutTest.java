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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("read-2-connection-2-timeout")
@SpringBootTest
public class RestTemplateWithTimeoutTest {

    @Autowired
    private RestTemplate restTemplateWithTimeout;

    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @DisplayName("read timeout 이 발생하면 ResourceAccessException 이 발생해야한다.")
    @Test
    void readtimeout() {

        // given
        mockWebServer.enqueue(
                new MockResponse()
                        .setBody("Hello World")
                        .setBodyDelay(3, TimeUnit.SECONDS)
        );
        String url = mockWebServer.url("/").toString();

        // when
        // then
        assertThatThrownBy(
                () -> restTemplateWithTimeout.getForObject(url, String.class)
        ).isInstanceOf(ResourceAccessException.class);
    }

    @DisplayName("read timeout 발생하지 않음")
    @Test
    void not_readtimeout() {

        mockWebServer.enqueue(
                new MockResponse()
                        .setBody("Hello World")
                        .setBodyDelay(1, TimeUnit.SECONDS)
        );
        String url = mockWebServer.url("/").toString();

        // when
        String result = restTemplateWithTimeout.getForObject(url, String.class);

        // then
        assertThat(result).isEqualTo("Hello World");
    }
}
