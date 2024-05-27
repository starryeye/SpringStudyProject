package dev.practice.restclient.clients.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class MyInterceptor implements ClientHttpRequestInterceptor {

    /**
     * ClientHttpRequestInterceptor 에서 할만한 작업
     * - logging
     * - Retry
     * - 각종 보안
     *      - OAuth2, https://github.com/spring-projects/spring-security/issues/13588#issuecomment-1881627223
     * - 등등..
     */

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        log.info("Intercepted request: {}", request.getURI());

        request.getHeaders().add("x-request-id", UUID.randomUUID().toString().substring(0, 8));

        return execution.execute(request, body);
    }
}
