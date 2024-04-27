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
     * 
     */
}
