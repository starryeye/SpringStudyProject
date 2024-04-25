package dev.practice.transactionboundevent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransactionboundeventApplication {

	/**
	 * Spring event 에 대해 알아본다.
	 * - Transaction bound event
	 * - Spring 4.2 버전 이후
	 *
	 * https://www.baeldung.com/spring-events
	 */

	public static void main(String[] args) {
		SpringApplication.run(TransactionboundeventApplication.class, args);
	}

}
