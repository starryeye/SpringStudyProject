package dev.practice.fetchjoin;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @Builder
    private Member(Long id, String name, Team team) {
        this.id = id;
        this.name = name;
        this.team = team;
    }

    public static Member create(String name) {
        return Member.builder()
                .id(null)
                .name(name)
                .build();
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
