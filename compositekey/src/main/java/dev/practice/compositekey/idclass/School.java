package dev.practice.compositekey.idclass;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@IdClass(SchoolId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class School {

    @Id
    private String regionNumber;
    @Id
    private String name;

    private LocalDate establishmentDate;

    @Builder
    private School(String regionNumber, String name, LocalDate establishmentDate) {
        this.regionNumber = regionNumber;
        this.name = name;
        this.establishmentDate = establishmentDate;
    }

    public static School create(String regionNumber, String name, LocalDate establishmentDate) {

        return School.builder()
                .regionNumber(regionNumber)
                .name(name)
                .establishmentDate(establishmentDate)
                .build();
    }
}
