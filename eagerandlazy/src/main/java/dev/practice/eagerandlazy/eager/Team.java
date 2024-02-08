package dev.practice.eagerandlazy.eager;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Builder
    private Team(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Team create(String name) {
        return Team.builder()
                .id(null)
                .name(name)
                .build();
    }
}
