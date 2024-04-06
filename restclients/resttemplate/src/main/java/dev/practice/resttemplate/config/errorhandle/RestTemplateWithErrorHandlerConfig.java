package dev.practice.resttemplate.config.errorhandle;

import dev.practice.resttemplate.config.RestTemplateProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@RequiredArgsConstructor
@Configuration
public class RestTemplateWithErrorHandlerConfig {

    private final RestTemplateProperties restTemplateProperties;

    @Bean
    public RestTemplate restTemplateWithErrorHandler(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(restTemplateProperties.getConnectionTimeout()))
                .setReadTimeout(Duration.ofMillis(restTemplateProperties.getReadTimeout()))
                .requestFactory( // 응답 body stream 을 로깅을 위한 읽기, 실제 응답 읽기로 2 회 읽기 위함
                        () -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory())
                )
                .errorHandler(new MyResponseErrorHandler()) // error handler
                .build();
    }
}
