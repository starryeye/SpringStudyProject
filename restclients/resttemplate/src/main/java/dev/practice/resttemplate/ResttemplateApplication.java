package dev.practice.resttemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResttemplateApplication {

	/**
	 * RestTemplate
	 *
	 * Spring MVC 에서 만들어서 사용가능한, 외부와의 HTTP 통신 도구이다.
	 *
	 * RestTemplate 을 설정없이 그대로 사용하는 것으로는 부족한 부분이 존재한다.
	 * 아래 설정을 추가로 해주면 좋다.
	 *
	 * 1. 타임아웃
	 * 기본 RestTemplate 은 timeout 의 제한이 없다. (ClientHttpRequestFactorySettings.DEFAULTS)
	 * -> 모든 쓰레드가 RestTemplate 으로 API를 호출하여 대기 상태에 빠진다면
	 * 다른 클라이언트 요청에 응답할 쓰레드가 남아있지 않게 된다.
	 * -> 일정 시간이 지나도 응답이 없다면, 연결을 강제로 끊어주도록 반드시 타임아웃 설정을 해주어야한다.
	 *
	 * 2. 재시도
	 *
	 * 3. 요청/응답 로깅
	 *
	 * 4. 에러 핸들링
	 *
	 * 5. HttpMessageConverter
	 */

	public static void main(String[] args) {
		SpringApplication.run(ResttemplateApplication.class, args);
	}

}
