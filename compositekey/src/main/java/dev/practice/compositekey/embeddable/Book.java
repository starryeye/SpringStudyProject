package dev.practice.compositekey.embeddable;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "region_number"),
            @JoinColumn(name = "name")
    })
    private Library library; // 비 식별 관계, Library 의 PK 가 Book 의 PK 는 아니고 FK 이다.

    private LocalDate createdAt;

    @Builder
    private Book(String id, Library library, LocalDate createdAt) {
        this.id = id;
        this.library = library;
        this.createdAt = createdAt;
    }

    public static Book create(Library library, LocalDate createdAt) {
        return Book.builder()
                .id(null)
                .library(library)
                .createdAt(createdAt)
                .build();
    }
}
