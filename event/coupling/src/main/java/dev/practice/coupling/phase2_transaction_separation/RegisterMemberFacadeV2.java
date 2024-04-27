package dev.practice.coupling.phase2_transaction_separation;

import dev.practice.coupling.dto.RegisterMemberRequest;
import dev.practice.coupling.dto.RegisterMemberResponse;
import dev.practice.coupling.phase2_transaction_separation.service.EmailServiceV2;
import dev.practice.coupling.phase2_transaction_separation.service.MemberServiceV2;
import dev.practice.coupling.repository.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterMemberFacadeV2 {

    private final MemberServiceV2 memberServiceV2;
    private final EmailServiceV2 emailServiceV2;

    public RegisterMemberResponse execute(RegisterMemberRequest request) {

        // 회원 등록
        Member registered = memberServiceV2.register(request);

        // 회원 등록 감사 메일 발송
        emailServiceV2.sendThankYouEmailForRegistered(registered.getId(), registered.getName(), registered.getEmail());

        // other logic..

        return RegisterMemberResponse.of(registered);
    }

    /**
     * phase 2 에서는 phase 1 코드에서 facade 계층을 통해 트랜잭션을 분리하였다.
     *
     * 이로써, phase 1 문제 중.. 어떤게 해결되었는지 보자..
     *
     * 1. 기능간 강결합
     * 해결되지 않음
     * MemberService 와 EmailService 간의 강결합에서
     * RegisterMemberFacade 와 EmailService 간의 강결합으로 변하였을 뿐..
     * 여전히.. EmailService 를 별도의 모듈이나 서비스로 분리하면 코드를 RegisterMemberFacade 코드를 수정해야한다.
     *
     * 2. transaction 경계
     * 해결됨
     * facade 계층에서는 트랜잭션을 걸지않고 MemberService, EmailService 에서 각각 트랜잭션을 걸고 있어서 병합되지 않는다.
     * 메인 로직인 회원 가입은 성공하고 메일 보내기 기능이 실패해도 메인 로직이 롤백되지는 않는다.
     *
     * 3. 동기적인 방식
     * 해결되지 않음
     * 회원 가입(저장)이 성공한 이후, 회원 가입 감사 메일 발송이 완료 되어야 회원 가입 응답을 보낼 수 있어서 여전히 동기 방식이다.
     * 비즈니스 상 회원 가입 응답은 회원 가입 감사 메일 발송 완료 여부 및 결과에 관심이 없다.
     *
     * 4. commit 시점과 메일 발송 시점
     * 해결됨
     * MemberService 의 트랜잭션 commit 이후 EmailService 트랜잭션이 시작되기 때문에
     * MemberService 트랜잭션이 롤백 되었는데 EmailService 가 수행되지는 않을 것이다.
     *
     *
     * 참고
     * 또 다른, Transaction 분리 방법
     * phase 1 코드에서 EmailService 의 @Transactional 에 REQUIRED_NEW 전파 옵션을 사용
     * 이렇게 하면 어떻게 될까..
     * 2 번은 해결될 수 있다.
     * 내부 트랜잭션 이라도 참여를 하지 않기 때문에 서로 다른 트랜잭션이 되어..
     * EmailService 에서 빠져나온 예외만 MemberService 에서 잘 처리해주면..
     * EmailService 가 롤백되더라도 MemberService 가 롤백되지는 않을 것이다.
     *
     * 4번은 해결되지 않는다.
     * 전파 옵션 변경으로 트랜잭션이 분리 되었지만, MemberService 내부에서
     * EmailService 를 호출 하기 때문에 MemberService 트랜잭션 commit 은 EmailService 로직을 완전히 처리한 이후이다.
     *
     */
}
