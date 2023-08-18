package dev.practice.nplusone.lazy;

import jakarta.persistence.*;
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
    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @Builder
    private Member(String name, Integer age, Team team) {
        this.name = name;
        this.age = age;
        this.team = team;
    }
}
