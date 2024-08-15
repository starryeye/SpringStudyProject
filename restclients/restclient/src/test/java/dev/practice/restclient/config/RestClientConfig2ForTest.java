package dev.practice.restclient.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@TestConfiguration
public class RestClientConfig2ForTest {

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }

    @Bean
    public RestClientCustomizer restClientCustomizer() {
        return restClientBuilder -> restClientBuilder
                .requestFactory(new JdkClientHttpRequestFactory())
                .baseUrl("https://jsonplaceholder.typicode.com");
    }
}
