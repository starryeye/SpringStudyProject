package dev.practice.coupling.repository;

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
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @Builder
    private Member(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static Member create(String name, String email) {
        return Member.builder()
                .id(null)
                .name(name)
                .email(email)
                .build();
    }
}
