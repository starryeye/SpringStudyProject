package dev.practice.eventandtransaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventandtransactionApplication {

	/**
	 * spring event 와 @Transactional 을 다뤄본다.
	 *
	 * https://www.baeldung.com/spring-events
	 */

	public static void main(String[] args) {
		SpringApplication.run(EventandtransactionApplication.class, args);
	}

//	@Bean
//	public ApplicationRunner runner(MyApplicationEventPublisher publisher) {
//		return args -> {
//				publisher.publishMyEvent("hello, world");
//		};
//	}
}
