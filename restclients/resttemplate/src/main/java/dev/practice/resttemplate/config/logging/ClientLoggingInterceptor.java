package dev.practice.resttemplate.config.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
public class ClientLoggingInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        final String requestId = generateRequestId();
        printRequest(requestId, request, body); // request logging

        ClientHttpResponse response = execution.execute(request, body); // execute

        printResponse(requestId, response); // response logging

        return response;
    }

    private String generateRequestId() {
        return Integer.toString((int) (Math.random() * 1000000));
    }

    private void printRequest(final String requestId, final HttpRequest req, final byte[] body) {

        log.info("[{}] URI: {}, Method: {}, Headers:{}, Body:{} ",
                requestId, req.getURI(), req.getMethod(), req.getHeaders(), new String(body, StandardCharsets.UTF_8));
    }

    private void printResponse(final String requestId, final ClientHttpResponse res) throws IOException {

        String body = new BufferedReader(new InputStreamReader(res.getBody(), StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining("\n"));

        log.info("[{}] Status: {}, Headers:{}, Body:{} ",
                requestId, res.getStatusCode(), res.getHeaders(), body);
    }
}
