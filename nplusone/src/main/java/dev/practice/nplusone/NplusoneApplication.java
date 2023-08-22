package dev.practice.nplusone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NplusoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(NplusoneApplication.class, args);
	}

	/**
	 * 객체는 양방향으로 참조가 있어야 서로 접근이 되지만..
	 *
	 * DB 는 FK 하나로 양방향 접근(조인)이 가능하다.
	 *
	 * N + 1 문제의 모든 케이스 정리해보기
	 */

}
