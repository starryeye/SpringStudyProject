package dev.practice.coupling.phase2_transaction_separation.service;

import dev.practice.coupling.dto.RegisterMemberRequest;
import dev.practice.coupling.repository.Member;
import dev.practice.coupling.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final MemberRepository memberRepository;

    @Transactional
    public Member register(RegisterMemberRequest request) {

        Member member = Member.create(request.name(), request.email());

        return memberRepository.save(member);
    }
}
