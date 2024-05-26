package dev.practice.restclient.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient defaultRestClient() {

        /**
         * static RestClient create()
         * static RestClient create(String baseUrl)
         * static RestClient create(RestTemplate restTemplate)
         */

        return RestClient.create();
    }

    @Bean
    public RestClient customRestClient(ClientHttpRequestFactory httpComponentsClientHttpRequestFactory) {

        return RestClient.builder()
                .requestFactory(httpComponentsClientHttpRequestFactory)
//                .messageConverters(converters -> converters.add(new MyCustomMessageConverter()))
                .baseUrl("https://example.com")
//                .defaultUriVariables(Map.of("variable", "foo"))
//                .defaultHeader("My-Header", "Foo")
//                .requestInterceptor(myCustomInterceptor)
//                .requestInitializer(myCustomInitializer)
                .build();
    }

    @Bean
    public ClientHttpRequestFactory jdkClientHttpRequestFactory() {

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(10000); // read timeout

        return requestFactory;
    }

    @Bean
    public ClientHttpRequestFactory httpComponentsClientHttpRequestFactory() {

        // 커넥션 풀 매니저 설정
        // todo, idle connection evict 설정 필요.. 유휴 커넥션 관리
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100); // 최대 커넥션 수
        connectionManager.setDefaultMaxPerRoute(20); // 라우트당 최대 커넥션 수

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectionRequestTimeout(Timeout.ofSeconds(1)) // connection timeout
                                .setResponseTimeout(Timeout.ofSeconds(2)) // response timeout
                                .build()
                )
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}
