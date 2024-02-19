package dev.practice.nplusone.eager;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamEager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String introduction;

    @OneToMany(mappedBy = "teamEager", fetch = FetchType.EAGER) // XXXToMany 에서는 Lazy 가 기본 값이긴 하다.
    private List<MemberEager> memberEagers = new ArrayList<>();

    @Builder
    private TeamEager(String name, String introduction, List<MemberEager> memberEagers) {
        this.name = name;
        this.introduction = introduction;
        this.memberEagers = memberEagers;
    }
}
