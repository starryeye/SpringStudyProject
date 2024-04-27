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
     * 
     */
}
