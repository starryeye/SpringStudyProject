package dev.practice.nplusone.subselect;

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

    /**
     * SubSelect 는 Eager, Lazy 모두 잘 동작한다.
     *
     * Eager : 쿼리 조회 시점에 N + 1 해결
     * Lazy : 프록시 초기화 시점에 N + 1 해결
     */
    @Fetch(value = FetchMode.SUBSELECT) // hibernate 에서 제공
    @OneToMany(mappedBy = "teamSub", fetch = FetchType.EAGER)
    private List<MemberSub> memberSubs = new ArrayList<>();

    @Builder
    private TeamSub(String name, String introduction, List<MemberSub> memberSubs) {
        this.name = name;
        this.introduction = introduction;
        this.memberSubs = memberSubs;
    }
}
