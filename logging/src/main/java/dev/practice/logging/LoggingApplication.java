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
	 *
	 * JsonTemplateLayout + integer type
	 * https://github.com/vy/log4j2-logstash-layout/issues/46
	 * https://logging.apache.org/log4j/2.12.x/manual/extending.html#Custom_ThreadContextMap_implementations
	 */

	public static void main(String[] args) {
		System.setProperty("log4j2.threadContextMap", "org.apache.logging.log4j.spi.CopyOnWriteSortedArrayThreadContextMap");
		SpringApplication.run(LoggingApplication.class, args);
	}

}
