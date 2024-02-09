package dev.practice.cascadeandorphan;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    @Builder
    private Team(Long id, String name, List<Member> members) {
        this.id = id;
        this.name = name;

        if(Objects.nonNull(members)) {
            members.forEach(this::addMember);
        }
    }

    public static Team create(String name) {
        return Team.builder()
                .id(null)
                .name(name)
                .build();
    }

    // 편의 메서드
    public void addMember(Member member) {
        this.members.add(member);
        member.setTeam(this);
    }
}
