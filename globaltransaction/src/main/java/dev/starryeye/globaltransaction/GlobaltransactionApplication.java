package dev.starryeye.globaltransaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GlobaltransactionApplication {

	/**
	 * JTA (Java Transaction API),
	 * 이종의 여러 자원에 걸친 분산 트랜잭션을 global transaction 으로 처리하기 위한 Java EE 기술이다.
	 * Atomikos 와 Bitronix 가 구현체의 대표로 존재한다. (javax(jakarta).transaction-api 를 구현)
	 *
	 * Spring boot 3 + Atomikos
	 * https://medium.com/@ygnhmt/distrubuted-transactions-springboot-3-atomikos-a5adfcba2c41
	 *
	 */

	public static void main(String[] args) {
		SpringApplication.run(GlobaltransactionApplication.class, args);
	}

}
