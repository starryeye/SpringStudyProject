package dev.practice.resttemplate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
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

    /**
     * maxAttempts 까지 재시도가 실패하면 default 값을 리턴하고 싶을땐
     * @Recover 를 활용하면 된다.
     *
     * @Retryable and @Recover 매핑 조건
     * - 메소드 시그니처 일치
     * @Recover 어노테이션이 붙은 메소드가 다른 클래스에 있더라도,
     * @Retryable 어노테이션과 연결되려면 예외 타입과 메소드 파라미터가 일치해야 한다. (리턴타입 포함)
     */
    @Recover
    public String recover(RuntimeException e) {
        log.info("Retry cause exception : ", e);
        return "All retry attempts are exhausted, recovery action";
    }
}
