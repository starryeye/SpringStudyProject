package dev.practice.OpenFeign.adapter.out.web;

import feign.Request;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MyFeignClientConfig {

    @Bean
    Request.Options feignRequestOptions() {
        return new Request.Options(
                5, TimeUnit.SECONDS, 5, TimeUnit.SECONDS, true
        );
    }

    @Bean
    ErrorDecoder feignErrorDecoder() {
        return new ErrorDecoder() {
            @Override
            public Exception decode(String methodKey, Response response) {
                switch (response.status()) {
                    case 400:
                        return new RuntimeException();
                    default:
                        return new IllegalStateException();
                }
            }
        };
    }
}
