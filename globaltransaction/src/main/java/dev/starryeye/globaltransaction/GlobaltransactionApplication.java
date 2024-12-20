package dev.starryeye.globaltransaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GlobaltransactionApplication {

    /**
     * JTA (Java Transaction API),
     * 이종의 여러 자원에 걸친 분산 트랜잭션을 global transaction 으로 처리하기 위한 Java EE 기술로, XA 아키텍처를 기반으로 분산 트랜잭션을 수행한다.
     * (혹은 데이터베이스 샤딩이 적용된 분산 데이터베이스도 포함)
     * Atomikos 와 Bitronix 가 구현체의 대표로 존재한다. (javax(jakarta).transaction-api 를 구현)
     *
     * 내부적으로 Two phase commit (2PC) 프로토콜을 통해 분산 트랜잭션을 관리한다.
     *
     * <p>
     * XA (eXtended Architecture)
     * 분산 트랜잭션 처리를 위한 사양인 eXtended Architecture 를 의미한다.
     * XA의 목표는 이기종 구성 요소가 포함된 전역 트랜잭션에 대한 원자성을 제공하는 것이다.
     * <p>
     * Atomikos, Bitronix 와 같은 JTA 구현체는 자체적인 트랜잭션 매니저와 커넥션 풀을 포함한다.
     * 이 경우, HikariCP 대신 JTA 구현체에서 제공하는 커넥션 풀을 사용하게 된다.
     * 예를 들어, Atomikos 를 사용하면 AtomikosDataSourceBean 을 통해 커넥션 풀을 관리한다.
     */

    public static void main(String[] args) {
        SpringApplication.run(GlobaltransactionApplication.class, args);
    }

    /**
     * Spring boot 3 + Atomikos
     * https://medium.com/@ygnhmt/distrubuted-transactions-springboot-3-atomikos-a5adfcba2c41
     *
     * NAVER D2
     * https://d2.naver.com/helloworld/5812258
     *
     */

}
