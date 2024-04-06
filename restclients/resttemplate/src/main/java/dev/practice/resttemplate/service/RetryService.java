package dev.practice.resttemplate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryService {

    private final RestTemplate restTemplateWithTimeout;

    // AppConfig 에 @EnableRetry 필수
    @Retryable(retryFor = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 100))
    public String execute(String url) {
        log.info("try request : {}", url);
        return restTemplateWithTimeout.getForObject(url, String.class);
    }
}
