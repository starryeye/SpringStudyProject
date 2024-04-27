package dev.practice.coupling.phase3_async.service;

import dev.practice.coupling.dto.RegisterMemberRequest;
import dev.practice.coupling.dto.RegisterMemberResponse;
import dev.practice.coupling.repository.Member;
import dev.practice.coupling.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceV3 {

    private final MemberRepository memberRepository;
    private final EmailServiceV3 emailServiceV3;

    public RegisterMemberResponse registerProcess(RegisterMemberRequest request) {

        // 회원 등록
        Member registered = register(request);

        // 회원 등록 감사 메일 발송
        emailServiceV3.sendThankYouEmailForRegistered(registered.getId(), registered.getName(), registered.getEmail());

        // other logic..

        return RegisterMemberResponse.of(registered);
    }

    private Member register(RegisterMemberRequest request) {
        Member member = Member.create(request.name(), request.email());
        return memberRepository.save(member);
    }

    /**
     * phase 3 에서는 phase 1 과 비교하여 EmailService 에서 @Async 를 활용하여
     * EmailService 를 비동기 처리하였다.
     *
     * 이로써, phase 1 문제 중.. 어떤게 해결되었는지 보자..
     *
     * 1. 기능간 강결합
     * 해결되지 않음
     * phase 1 이유와 동일
     *
     * 2. transaction 경계
     * 해결됨
     * 트랜잭션은 ThreadLocal 기반으로 동일 Connection 내에서 동작한다.
     * @Async 로 서로 다른 스레드로 동작하도록 만들었기 때문에 트랜잭션이 분리되었다.
     * 메인 로직인 회원 가입은 성공하고 메일 보내기 기능이 실패해도 메인 로직이 롤백되지는 않는다.
     *
     * 3. 동기적인 방식
     * 해결됨
     * MemberService 로직에서 EmailService 를 호출하지만, @Async 로 인해 서로 다른 스레드로 동작하며 (non-blocking)
     * 코드상 MemberService 은 EmailService 완료 시점 및 결과에 아무런 관심이 없어졌으므로 비동기이다.
     * -> EmailService 처리 시간은 API 응답 시간에 영향을 주지 않게 되었다.
     *
     * 4. commit 시점과 메일 발송 시점
     * 해결되지 않음
     * 스레드가 분리 되어도..
     * 현재 코드 구조상 phase 2 의 두번째 방법(@Transactional 에 REQUIRED_NEW 전파 옵션을 사용) 과 마찬가지로
     * MemberService 내부에서 EmailService 를 호출 하기 때문에
     * MemberService 트랜잭션 commit 은 EmailService 로직을 호출한 이후에 수행된다.
     * 결국, phase 1 의 참고에서 말한 문제가 생길 수 있음
     * -> 근데, facade 계층을 두면 해결해볼 순 있음..
     *
     */
}
