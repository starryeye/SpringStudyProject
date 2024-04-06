package dev.practice.resttemplate.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@RequiredArgsConstructor
@Configuration
public class RestTemplateWithRetryConfig {

    private final RestTemplateProperties restTemplateProperties;

    @Bean
    public RestTemplate restTemplateWithRetry(RestTemplateBuilder restTemplateBuilder) {
        /**
         * retry, timeout 설정
         *
         * spring-retry 의존성이 필요하다.
         * request interceptor 를 통해 retry 를 적용하였다.
         *
         * [다른 방법]
         * @Service 로직이나 restTemplate 을 사용하는 메서드에서 @Retryable 을 사용하자
         * -> 이 경우엔 여기서 interceptor 에 retry 를 추가한 녀석을 쓰면 중복임
         * -> @Retryable 은 aop 로 동작한다.
         * -> @Retryable 은 restTemplate 과 상관없는 기능임 그냥 "재시도" 그자체임
         */

        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(restTemplateProperties.getConnectionTimeout()))
                .setReadTimeout(Duration.ofMillis(restTemplateProperties.getReadTimeout()))
                .additionalInterceptors(clientHttpRequestInterceptor()) // 요청 전에 무슨 일(retry)을 하도록 함.
                .build();
    }

    public ClientHttpRequestInterceptor clientHttpRequestInterceptor() {

        return (request, body, execution) -> {

            // retry template
            RetryTemplate retryTemplate = RetryTemplate.builder()
                    .maxAttempts(restTemplateProperties.getRetryCount()) // 최대 3회
                    .fixedBackoff(restTemplateProperties.getBackoff().longValue()) // Backoff : 실패 후 다음 재시도까지 대기 시간
                    .build();

            // 아래와 같이 RetryPolicy 의 구현체(SimpleRetryPolicy, circuit breaker, max attempt 등) 을 직접 넣어줘도 된다.
//            retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3)); // 최대 3회 시도

            try {
                return retryTemplate.execute(context -> execution.execute(request, body));
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        };
    }
}
