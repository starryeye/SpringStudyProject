package dev.practice.compositekey.idclass;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@IdClass(SchoolId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class School {

    @Id
    private String regionNumber;
    @Id
    private String name;

    @Builder
    private School(String regionNumber, String name) {
        this.regionNumber = regionNumber;
        this.name = name;
    }

    public static School create(String regionNumber, String name) {
        return School.builder()
                .regionNumber(regionNumber)
                .name(name)
                .build();
    }
}
