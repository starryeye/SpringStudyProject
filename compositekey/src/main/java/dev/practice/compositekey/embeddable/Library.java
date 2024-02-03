package dev.practice.compositekey.embeddable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Library {

    @EmbeddedId
    private LibraryId id;

    private LocalDate establishmentDate;

    @Builder
    private Library(LibraryId id, LocalDate establishmentDate) {
        this.id = id;
        this.establishmentDate = establishmentDate;
    }

    public static Library create(String regionNumber, String name, LocalDate establishmentDate) {

        LibraryId id = LibraryId.builder()
                .regionNumber(regionNumber)
                .name(name)
                .build();

        return Library.builder()
                .id(id)
                .establishmentDate(establishmentDate)
                .build();
    }
}
