package dev.practice.nplusone.subselect;

import dev.practice.nplusone.lazy.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamSub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String introduction;

    @Fetch(FetchMode.SUBSELECT) // hibernate 에서 제공
    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER) // SubSelect 는 Eager 에서도 잘 동작한다. 쿼리 조회 시점에 N + 1 해결
    private List<Member> members = new ArrayList<>();

    @Builder
    private TeamSub(String name, String introduction, List<Member> members) {
        this.name = name;
        this.introduction = introduction;
        this.members = members;
    }
}
