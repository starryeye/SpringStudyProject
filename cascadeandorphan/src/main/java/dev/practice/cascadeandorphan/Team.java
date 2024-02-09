package dev.practice.cascadeandorphan;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    @Builder
    private Team(Long id, String name, List<Member> members) {
        this.id = id;
        this.name = name;
        addMembers(members);
    }

    public static Team create(String name) {
        return Team.builder()
                .id(null)
                .name(name)
                .build();
    }

    // 편의 메서드
    public void addMembers(List<Member> members) {

        if(Objects.nonNull(members)) {
            members.forEach(
                    member -> {
                        this.members.add(member); // cascade 를 이용하려면 add 를 해줘야한다. (CascadeTest, persist2 코드 확인)
                        member.setTeam(this);
                    }
            );
        }
    }

    public void removeMemberByMemberName(String name) {

        if(Objects.nonNull(this.members)) {
            this.members.stream()
                    .filter(member -> member.getName().equals(name))
                    .findFirst()
                    .ifPresent(
                            member -> this.members.remove(member)
                    );
        }
    }
}
