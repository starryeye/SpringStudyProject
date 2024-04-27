package dev.practice.coupling.phase1.service;

import dev.practice.coupling.dto.RegisterMemberRequest;
import dev.practice.coupling.dto.RegisterMemberResponse;
import dev.practice.coupling.repository.Member;
import dev.practice.coupling.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceV1 {

    private final MemberRepository memberRepository;
    private final EmailServiceV1 emailServiceV1;

    @Transactional
    public RegisterMemberResponse registerProcess(RegisterMemberRequest request) {

        // 회원 등록
        Member registered = register(request);

        // 회원 등록 감사 메일 발송
        emailServiceV1.sendThankYouEmailForRegistered(registered.getId(), registered.getName(), registered.getEmail());

        // other logic..

        return RegisterMemberResponse.of(registered);
    }

    private Member register(RegisterMemberRequest request) {
        Member member = Member.create(request.name(), request.email());
        return memberRepository.save(member);
    }

    /**
     * Phase 1 의 문제점
     * 1. 기능간 강결합
     * MemberService 와 EmailService 는 강한 결합을 가지고 있다.
     * 예를 들어, EmailService 가 별도의 모듈이나 별도의 시스템으로 분리되어야 할 경우 MemberService 의 코드를 변경해야한다.
     * -> 기능간 결합을 느슨하게 가져가야할 필요성 대두
     *
     * 2. transaction 경계
     * MemberService::registerProcess 와 EmailService::sendThankYouEmailForRegistered 는 각 메서드가 @Transactional 을 가지지만,
     * 호출 흐름 상 @Transactional 전파 옵션이 default(REQUIRED) 로 병합이 된다.
     * 따라서, 메인 로직인 회원 가입은 성공하여도 메일 보내기 기능이 실패하면 메인 로직이 롤백해야하는 상황
     * -> 트랜잭션 분리 필요성 대두
     *
     * 3. 동기적인 방식
     * 회원 가입과 회원 가입 감사 메일 발송은 동기적으로 처리 될 필요는 없다.
     * 회원 가입을 성공해야 메일 발송을 할 수 있으므로 병렬적으로 처리할 수는 없지만,
     * 회원 가입이 성공하면 MemberService::registerProcess 입장에서는 메일 발송 성공 여부에 대한 관심은 없으므로
     * 응답 보내는 처리와 메일 발송은 비동기로 처리해 볼 수 있을 것이다.
     * ->  메일 발송을 비동기로 처리 필요성 대두
     *
     * 4. commit 시점과 메일 발송 시점
     * MemberService::registerProcess 메서드 범위에 transaction 을 걸고 있기 때문에
     * registerProcess 가 모두 완료되고 예외가 없을 때, commit 명령어를 날린다.
     * 메인 로직인 회원 가입 save 명령어를 호출하는 시점에는 별 문제가 없었고 메일 발송도 문제가 없었는데
     * 모종의 이유로 메일 발송 이후와 registerProcess 메서드 완료 사이 시점(// other logic..)에 문제가 발생하면..
     * 회원 가입, 메일 히스토리 저장은 롤백 되겠지만, 메일 발송은 이미 성공적으로 처리가 된 이후이다..
     * -> 메일 발송을 commit 이 성공한 뒤로 미뤄야하는 필요성 대두
     *
     * 참고
     * // other logic.. 은 제 3의 로직일 수 도 있지만,
     * member 기본 키 생성 전략을 sequence 로 하여 insert 쿼리의 쓰기 지연이라 생각하자..
     * -> 쓰기지연 적용된 insert 쿼리를 날렸는데 DB 에서 어떤 컬럼의 유니크 제약이라던지.. 등으로 인해 exception 발생가능성 존재
     * 제 3의 로직이라 생각하면, 회원 가입(저장)은 성공했는데 제 3의 로직이 실패하여 회원 가입(저장) 까지 롤백되는 문제로.. 즉, 2번 문제이다.
     */
}
