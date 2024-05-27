package dev.starryeye.httpinterfaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HttpinterfacesApplication {

	/**
	 * Http Interface
	 *
	 * https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface
	 *
	 * todo, error handling..
	 *
	 *
	 * project 설명
	 * UserHttpInterfaceClient 와 UserRestClient 는 동일하다.
	 */

	public static void main(String[] args) {
		SpringApplication.run(HttpinterfacesApplication.class, args);
	}

}
