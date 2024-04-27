package dev.practice.coupling.phase4_event.service;

import dev.practice.coupling.dto.RegisterMemberRequest;
import dev.practice.coupling.dto.RegisterMemberResponse;
import dev.practice.coupling.phase4_event.event.RegisteredMemberEvent;
import dev.practice.coupling.repository.Member;
import dev.practice.coupling.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceV4 {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public RegisterMemberResponse registerProcess(RegisterMemberRequest request) {

        // 회원 등록
        Member member = Member.create(request.name(), request.email());
        Member registered = memberRepository.save(member);

        // 회원 등록 이벤트 발행
        RegisteredMemberEvent event = RegisteredMemberEvent.create(registered.getId(), registered.getName(), registered.getEmail());
        eventPublisher.publishEvent(event);

        return RegisterMemberResponse.of(registered);
    }
}
