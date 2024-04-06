package dev.practice.resttemplate.config.errorhandle;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class MyResponseErrorHandler extends DefaultResponseErrorHandler {

    /**
     * 기본으로 적용되는 DefaultResponseErrorHandler 를 상속하여 오버라이딩을 통해
     * HTTP 수준의 status code 에 따른 커스텀 처리를 해볼 수 있다.
     *
     * 참고
     * IO 에러는 ResponseErrorHandler 로 처리가 불가능하다.
     * 따라서, RestTemplate::execute 호출 라인에서 try-catch 로 ResourceAccessException 를 잡아줘야한다.
     * (+ RestClientException)
     */

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        // HTTP 상태 코드
        HttpStatus statusCode = (HttpStatus) response.getStatusCode();
        // 응답 본문 읽기
        String body = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));

        // 4xx 클라이언트 에러 처리
        if (statusCode.series() == HttpStatus.Series.CLIENT_ERROR) {
            throw new HttpClientErrorException(statusCode, "Client error, status code : " + statusCode + ", response body : " + body);
        }
        // 5xx 서버 에러 처리
        else if (statusCode.series() == HttpStatus.Series.SERVER_ERROR) {
            throw new HttpServerErrorException(statusCode, "Server error, status code : " + statusCode + ", response body : " + body);
        }
    }
}
