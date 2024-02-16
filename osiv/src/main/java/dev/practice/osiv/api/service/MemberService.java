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


    /// 아래는 test code 용

    @Transactional
    public Long join(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public Member findOne(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }
}
