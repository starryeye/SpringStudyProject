package dev.practice.resttemplate.config.timeout;

import dev.practice.resttemplate.config.RestTemplateProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateWithTimeoutConfig {

    /**
     * Spring 은 기본적으로 AutoConfiguration 에 의해
     * RestTemplateBuilder 를 빈으로 등록해두므로 RestTemplate 빈 등록시 이용하면 된다.
     *
     * RestTemplateProperties 는 Spring 이 제공하는 빈이 아니라
     * @ConfigurationProperties 사용을 위한 클래스이다. (application.yml 값을 읽음)
     */

    /**
     * timeout
     *
     * [connection timeout]
     * client 가 server 에 연결을 시도할 때까지 기다리는 최대 시간을 의미한다.
     * -> 전체 3-way handshaking 이 수행되는 시간이다.
     * (일반적으로 syn, syn-ack, ack 중.. syn 보내고 syn-ack 받는데 까지의 시간이다.)
     * 이 시간이 지나면, client 는 더 이상의 대기 없이 연결 시도를 중단하고, 연결이 timeout 되었다고 간주
     *
     * 참고 - server 측의 accept 와의 관계
     * 1. server 는 listen 상태에서 특정 포트를 통해 들어오는 연결 요청을 기다린다.
     * 2. client 로부터 연결 요청(SYN 패킷)이 도착하면,
     * server 는 이 요청을 처리하고 client 에게 SYN-ACK 패킷을 보낸다. 이 단계에서 accept 함수가 연결 요청을 받아들이는 역할을 한다.
     * 3. client 가 ACK 패킷을 server 에 보내면, server 는 이를 받아들이고 연결 성공
     * 이때 accept 함수는 새로운 소켓을 반환하며, 이 소켓을 통해 server 와 client 간의 통신이 이루어진다.
     *
     * [read timeout]
     * connection 이 성공된 후(connection timeout 과 관련),
     * client 가 요청을 보내는 시점부터 server 로 부터 응답을 받기까지의 시간
     * 이 timeout 을 초과하면, client 는 읽기 작업을 중단하고 timeout 예외를 발생
     */

    @Bean // with timeout
    public RestTemplate restTemplateWithTimeout(
            RestTemplateBuilder restTemplateBuilder,
            RestTemplateProperties restTemplateProperties
    ) {
        // 일반적으로, "connection timeout"과 "read timeout"을 5초씩 잡아준다.
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(restTemplateProperties.getConnectionTimeout()))
                .setReadTimeout(Duration.ofMillis(restTemplateProperties.getReadTimeout()))
                .build();
    }
}
