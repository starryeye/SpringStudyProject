package dev.practice.osiv.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @Builder
    private Notice(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    public static Notice create(String message) {
        return Notice.builder()
                .id(null)
                .message(message)
                .build();
    }

    public static String generateCreateMemberMessage(String name) {
        return name + "님이 가입했습니다.";
    }
}
