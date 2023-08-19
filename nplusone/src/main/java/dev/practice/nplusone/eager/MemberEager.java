package dev.practice.nplusone.eager;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;

    @ManyToOne(fetch = FetchType.EAGER)
    private TeamEager teamEager;

    @Builder
    private MemberEager(String name, Integer age, TeamEager teamEager) {
        this.name = name;
        this.age = age;
        this.teamEager = teamEager;
    }
}
