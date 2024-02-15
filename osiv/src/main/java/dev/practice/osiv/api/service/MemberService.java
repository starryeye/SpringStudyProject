package dev.practice.osiv.api.service;

import dev.practice.osiv.domain.Member;
import dev.practice.osiv.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member create(String name) {

        Member member = Member.create(name);

        return memberRepository.save(member);
    }
}
