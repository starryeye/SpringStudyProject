package dev.practice.resttemplate.config.retry;

import dev.practice.resttemplate.config.RestTemplateProperties;
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
         * interceptor 에서 retry 를 하는 것은 400, 500 예외를 잡을 수 없다. (HttpServerErrorException, HttpClientErrorException)
         * -> 따라서, interceptor 에서 직접 status code 를 까서 확인해보던가(?) 아래의 다른 방법을 통해 구현하도록 하자
         *
         * [다른 방법]
         * @Service 로직이나 restTemplate 을 사용하는 메서드에서 @Retryable 을 사용하자
         * -> 이 경우엔 여기서 interceptor 에 retry 를 추가한 녀석을 쓰면 중복임
         * -> @Retryable 은 aop 로 동작한다.
         * -> @Retryable 은 restTemplate 과 상관없는 기능임 그냥 "재시도" 그자체
         */

        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(restTemplateProperties.getConnectionTimeout()))
                .setReadTimeout(Duration.ofMillis(restTemplateProperties.getReadTimeout()))
                .additionalInterceptors(clientHttpRequestInterceptor()) // 요청 전에 무슨 일(retry)을 하도록 함.
                .build();
    }

    private ClientHttpRequestInterceptor clientHttpRequestInterceptor() {

        return (request, body, execution) -> {

            System.out.println("Interceptor called. URL: " + request.getURI());

            // retry template
            RetryTemplate retryTemplate = RetryTemplate.builder()
                    .maxAttempts(restTemplateProperties.getRetryCount()) // 최대 3회
                    .fixedBackoff(restTemplateProperties.getBackoff().longValue()) // Backoff : 실패 후 다음 재시도까지 대기 시간
                    .retryOn(Exception.class)
                    .build();

            // 아래와 같이 RetryPolicy 의 구현체(SimpleRetryPolicy, circuit breaker, max attempt 등) 을 직접 넣어줘도 된다.
//            retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3)); // 최대 3회 시도

            /**
             * 여기서의 exception 은 5xx, 4xx 응답 코드에 따른
             * HttpServerErrorException, HttpClientErrorException 는 포함되지 않는다.
             * interceptor 를 통과 하고 난 이후에 생성되는 예외기 때문이다....
             */
            return retryTemplate.execute(
                    context -> {
                        System.out.println("Attempting request. Attempt number: " + context.getRetryCount());
                        try {
                            return execution.execute(request, body);
                        } catch (Exception e) {
                            System.out.println("Exception during request execution: " + e.getMessage());
                            throw e; // 예외를 다시 throw
                        }
                    }
            );
        };
    }
}
