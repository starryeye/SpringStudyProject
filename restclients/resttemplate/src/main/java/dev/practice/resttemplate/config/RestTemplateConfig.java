package dev.practice.resttemplate.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    /**
     * Spring 은 기본적으로 AutoConfiguration 에 의해
     * RestTemplateBuilder 를 빈으로 등록해두므로 RestTemplate 빈 등록시 이용하면 된다.
     */

    @Bean // with timeout
    public RestTemplate restTemplateWithTimeout(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }
}
