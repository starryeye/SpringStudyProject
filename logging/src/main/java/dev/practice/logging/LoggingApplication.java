package dev.practice.logging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoggingApplication {

	/**
	 * Log4j2 Guide..
	 *
	 * JsonTemplateLayout
	 * https://logging.apache.org/log4j/2.x/manual/json-template-layout.html
	 * https://logging.apache.org/log4j/2.x/manual/layouts.html#json-template-layout
	 *
	 * JsonLayout
	 * https://logging.apache.org/log4j/2.x/manual/layouts.html#JSONLayout
	 *
	 * Pattern
	 * https://logging.apache.org/log4j/2.x/manual/layouts.html#PatternLayout
	 */

	public static void main(String[] args) {
		SpringApplication.run(LoggingApplication.class, args);
	}

}
