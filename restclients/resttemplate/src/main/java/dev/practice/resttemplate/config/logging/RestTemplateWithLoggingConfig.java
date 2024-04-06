package dev.practice.resttemplate.config.logging;

import dev.practice.resttemplate.config.RestTemplateProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@RequiredArgsConstructor
@Configuration
public class RestTemplateWithLoggingConfig {

    private final RestTemplateProperties restTemplateProperties;

    @Bean
    public RestTemplate restTemplateWithLogging(RestTemplateBuilder restTemplateBuilder) {

        // timeout, retry, logging 적용

        return restTemplateBuilder
                .setReadTimeout(Duration.ofMillis(restTemplateProperties.getReadTimeout()))
                .setConnectTimeout(Duration.ofMillis(restTemplateProperties.getConnectionTimeout()))
                .additionalInterceptors( // retry, logging Interceptor 추가
                        clientHttpRequestInterceptor(),
                        new ClientLoggingInterceptor()
                )
                .requestFactory( // 응답 body stream 을 로깅을 위한 읽기, 실제 응답 읽기로 2 회 읽기 위함
                        () -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory())
                )
                .build();
    }

    // logging


    // retry
    private ClientHttpRequestInterceptor clientHttpRequestInterceptor() {

        return (request, body, execution) -> {

            // retry template
            RetryTemplate retryTemplate = RetryTemplate.builder()
                    .maxAttempts(restTemplateProperties.getRetryCount()) // 최대 3회
                    .fixedBackoff(restTemplateProperties.getBackoff().longValue()) // Backoff : 실패 후 다음 재시도까지 대기 시간
                    .build();

            // 아래와 같이 RetryPolicy 의 구현체(SimpleRetryPolicy, circuit breaker, max attempt 등) 을 직접 넣어줘도 된다.
//            retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3)); // 최대 3회 시도

            // 여기서 사용자 정의 예외로 바꿔주거나 처리해도 될 것이다.
            return retryTemplate.execute(context -> execution.execute(request, body));
        };
    }
}
