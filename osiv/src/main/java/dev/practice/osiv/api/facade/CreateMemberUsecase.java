package dev.practice.osiv.api.facade;

import dev.practice.osiv.api.service.MemberService;
import dev.practice.osiv.api.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateMemberUsecase {

    private final MemberService memberService;
    private final NoticeService noticeService;

    public void run() {

    }
}
