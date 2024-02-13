package dev.practice.pagingandsorting.service;

import dev.practice.pagingandsorting.repository.Member;
import dev.practice.pagingandsorting.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Page<Member> findMembersByName(String name, Pageable pageable) {

        return memberRepository.findPageByName(name, pageable);
    }
}
