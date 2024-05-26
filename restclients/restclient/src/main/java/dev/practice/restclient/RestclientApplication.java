package dev.practice.restclient;

import dev.practice.restclient.clients.MyPostClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestclientApplication {

	public static void main(String[] args) {

		System.setProperty("jdk.httpclient.HttpClient.log", "all"); // JdkClientHttpRequestFactory 용 로깅

		SpringApplication.run(RestclientApplication.class, args);
	}

	/**
	 * todo,
	 *  Error Handling..
	 */

//	@Bean
//	public ApplicationRunner applicationRunner(MyPostClient myPostClient) {
//		return args -> {
//			String posts = myPostClient.findAll();
//			System.out.println(posts);
//		};
//	}
}
