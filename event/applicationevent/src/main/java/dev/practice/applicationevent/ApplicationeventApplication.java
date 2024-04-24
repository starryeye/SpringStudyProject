package dev.practice.applicationevent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationeventApplication {

	/**
	 * spring event 를 다뤄본다. (Spring 4.2 버전 이전 방식)
	 *
	 * https://www.baeldung.com/spring-events
	 */

	public static void main(String[] args) {
		SpringApplication.run(ApplicationeventApplication.class, args);
	}

}
