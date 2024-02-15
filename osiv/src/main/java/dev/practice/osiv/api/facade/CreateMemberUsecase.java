package dev.practice.osiv.api.facade;

import dev.practice.osiv.api.service.MemberService;
import dev.practice.osiv.api.service.NoticeService;
import dev.practice.osiv.domain.Member;
import dev.practice.osiv.domain.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateMemberUsecase {

    private final MemberService memberService;
    private final NoticeService noticeService;

    public void run(String name) {

        /**
         *
         * memberService 와 noticeService 는
         * 동일한 영속성 컨텍스트를 공유하지만
         * 서로 다른 트랜잭션이다.
         */

        Member member = memberService.create(name);

        member.changeName("change name");
        String changed = member.getName();
        String message = Notice.generateCreateMemberMessage(changed);

        noticeService.create(message);
    }
}
