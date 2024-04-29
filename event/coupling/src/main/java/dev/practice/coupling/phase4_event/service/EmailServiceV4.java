package dev.practice.coupling.phase4_event.service;

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
public class EmailServiceV4 {

    private final EmailSender emailSender;
    private final EmailSendHistoryRepository emailSendHistoryRepository;

    private static final String THANK_YOU = "%s, Thank you for using our service! (phase 4)";

    @Transactional
    public void sendThankYouEmailForRegistered(Long memberId, String memberName, String emailAddress) {

        // 감사 메일 발송
        String message = THANK_YOU.formatted(memberName);
        emailSender.send(emailAddress, message);

        // 감사 메일 발송 히스토리 저장
        EmailSendHistory emailSendHistory = EmailSendHistory.create(
                message, memberId, LocalDateTime.now(), EmailType.MEMBER_REGISTER_SUCCESS
        );
        emailSendHistoryRepository.save(emailSendHistory);
    }

    /**
     * 메일 발송과 메일 발송 히스토리 의 결합 체크
     *
     * 비즈니스상 둘중 하나만 성공 혹은 실패는 이상함
     * -> 동일한 트랜잭션
     *
     * 위의 이유에 더하여..
     * 메일 발송의 성공 혹은 실패 여부에 따라 히스토리를 저장해야하므로 (결과에 관심)
     * 동기적인 관계를 가진다.
     */
}
