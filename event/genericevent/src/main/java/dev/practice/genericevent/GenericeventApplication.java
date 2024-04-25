package dev.practice.genericevent;

import dev.practice.genericevent.publisher.GenericSpringEventPublisher;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GenericeventApplication {

	/**
	 * Spring Event 에 대해 알아본다.
	 * - spring 4.2 이후 버전
	 * - ApplicationEvent 에 대한 의존성을 없앨 수 있다.
	 *
	 * https://www.baeldung.com/spring-events
	 */

	public static void main(String[] args) {
		SpringApplication.run(GenericeventApplication.class, args);
	}

}
