package dev.practice.nplusone.subselect;

import dev.practice.nplusone.lazy.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;

    @Fetch(FetchMode.SUBSELECT) // hibernate 에서 제공
    @ManyToOne(fetch = FetchType.LAZY) // SubSelect 는 Lazy 에서도 잘 동작한다. 지연 로딩 시점에 N + 1 해결
    private Team team;

    @Builder
    private MemberSub(String name, Integer age, Team team) {
        this.name = name;
        this.age = age;
        this.team = team;
    }
}
