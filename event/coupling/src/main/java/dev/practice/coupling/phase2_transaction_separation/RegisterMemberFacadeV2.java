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

        return RegisterMemberResponse.of(registered);
    }

    /**
     * 
     */
}
