package dev.practice.nplusone.subselect;

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

//    @Fetch(value = FetchMode.SUBSELECT) // Subselect 는 ManyToOne 관계에서는 사용할 수 없다. Join 은?
    @ManyToOne(fetch = FetchType.LAZY)
    private TeamSub teamSub;

    @Builder
    public MemberSub(String name, Integer age, TeamSub teamSub) {
        this.name = name;
        this.age = age;
        this.teamSub = teamSub;
    }
}
