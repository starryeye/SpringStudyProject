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
}
