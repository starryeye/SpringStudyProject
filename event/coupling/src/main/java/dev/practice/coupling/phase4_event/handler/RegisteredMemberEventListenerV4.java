package dev.practice.coupling.phase4_event.handler;

import dev.practice.coupling.phase4_event.event.RegisteredMemberEvent;
import dev.practice.coupling.phase4_event.service.EmailServiceV4;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RegisteredMemberEventListenerV4 {

    private final EmailServiceV4 emailServiceV4;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRegisteredMemberEvent(RegisteredMemberEvent event) {

        emailServiceV4.sendThankYouEmailForRegistered(event.id(), event.name(), event.email());
    }

    /**
     * Member 와 Email 서비스를 서로 다른 시스템으로 분리하게 된다면
     * 이 코드 부터 EmailService 를 분리시키면 되고
     * 이 코드로 인해 Email 서비스는 Member 이벤트를 구독하는 형태가 된다.
     *
     * 기존에 MemberService 에서 EmailService 를 호출하는 형태에서 (phase 1)
     * phase 4 에서는 EmailService 가 Member 이벤트를 구독하는 형태가 되었다.
     */
}
