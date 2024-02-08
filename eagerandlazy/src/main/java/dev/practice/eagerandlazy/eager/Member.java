package dev.practice.eagerandlazy.eager;

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

    @ManyToOne(fetch = FetchType.EAGER) // ManyToOne 은 default 가 즉시 로딩이다.
    @JoinColumn(nullable = false) // nullable 로 하면 즉시 로딩시 내부조인으로 수행된다. (기본 값은 true 이며 외부조인, 외부조인이 성능상 더 안좋다고 한다.)
    private Team team;

    @Builder
    private Member(Long id, String name, Team team) {
        this.id = id;
        this.name = name;
        this.team = team;
    }

    public static Member create(String name, Team team) {
        return Member.builder()
                .id(null)
                .name(name)
                .team(team)
                .build();
    }
}
