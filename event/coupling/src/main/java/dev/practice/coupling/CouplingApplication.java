package dev.practice.coupling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CouplingApplication {

	/**
	 * 예제 비즈니스 - 회원 가입
	 * -> 1. DB 에 회원 정보 저장 (main)
	 * -> 2. 회원 가입 감사 이메일 보내기, DB 에 회원 가입 감사 이메일 히스토리 저장 (sub)
	 *
	 *
	 * 목적
	 * 회원 가입 비즈니스를 가지고 하나의 모듈 내에서
	 * tight coupling 된 상황을 알아보고 loose coupling 해본다.
	 *
	 *
	 *
	 * 각 단계에 대한 부가 설명은 아래를 참조
	 * phase1 -> MemberServiceV1
	 * phase2 -> RegisterMemberFacadeV2
	 * phase3 -> MemberServiceV3
	 * phase4 -> MemberServiceV4, EmailServiceV4
	 *
	 * 참고해보면 좋음
	 * https://youtu.be/b65zIH7sDug?si=rvxZui-mR8-a3k_S
	 * https://techblog.woowahan.com/7835
	 */

	public static void main(String[] args) {
		SpringApplication.run(CouplingApplication.class, args);
	}

}
