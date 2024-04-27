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
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;

    @Transactional
    public RegisterMemberResponse registerProcess(RegisterMemberRequest request) {

        // 회원 등록
        Member registered = register(request);

        // 회원 등록 감사 메일 발송
        emailService.sendThankYouEmailForRegistered(registered.getId(), registered.getName(), registered.getEmail());

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
     * 모종의 이유로 commit 시점에 문제가 발생하면.. 회원 가입은 롤백이 되겠지만, 메일 발송은 이미 성공적으로 처리가 된 이후이다..
     * -> 메일 발송을 commit 이 성공한 뒤로 미뤄야하는 필요성 대두
     *
     */
}
