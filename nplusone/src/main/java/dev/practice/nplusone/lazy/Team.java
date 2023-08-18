package dev.practice.nplusone.lazy;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String introduction;

    @OneToMany(mappedBy = "team")
    private List<Member> members;

    @Builder
    private Team(String name, String introduction) {
        this.name = name;
        this.introduction = introduction;
    }
}
