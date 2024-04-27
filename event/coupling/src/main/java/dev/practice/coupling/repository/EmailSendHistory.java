package dev.practice.coupling.repository;

import dev.practice.coupling.repository.enums.EmailType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailSendHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private Long targetMemberId;

    private LocalDateTime sendAt;

    @Enumerated(EnumType.STRING)
    private EmailType emailType;

    @Builder
    private EmailSendHistory(Long id, String message, Long targetMemberId, LocalDateTime sendAt, EmailType emailType) {
        this.id = id;
        this.message = message;
        this.targetMemberId = targetMemberId;
        this.sendAt = sendAt;
        this.emailType = emailType;
    }

    public static EmailSendHistory create(String message, Long targetMemberId, LocalDateTime sendAt, EmailType emailType) {
        return EmailSendHistory.builder()
                .id(null)
                .message(message)
                .targetMemberId(targetMemberId)
                .sendAt(sendAt)
                .emailType(emailType)
                .build();
    }
}
