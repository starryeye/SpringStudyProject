package dev.practice.coupling.phase2_transaction_separation.service;

import dev.practice.coupling.network.EmailSender;
import dev.practice.coupling.repository.EmailSendHistory;
import dev.practice.coupling.repository.EmailSendHistoryRepository;
import dev.practice.coupling.repository.enums.EmailType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailServiceV2 {

    private final EmailSender emailSender;
    private final EmailSendHistoryRepository emailSendHistoryRepository;

    private static final String THANK_YOU = "%s, Thank you for using our service!";

    @Transactional
    public void sendThankYouEmailForRegistered(Long memberId, String memberName, String emailAddress) {

        // 감사 메일 발송
        String message = String.format(THANK_YOU, memberName);
        emailSender.send(emailAddress, message);

        // 감사 메일 발송 히스토리 저장
        EmailSendHistory emailSendHistory = EmailSendHistory.create(
                message, memberId, LocalDateTime.now(), EmailType.MEMBER_REGISTER_SUCCESS
        );
        emailSendHistoryRepository.save(emailSendHistory);
    }
}
