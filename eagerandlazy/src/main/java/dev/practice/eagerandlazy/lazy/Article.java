package dev.practice.eagerandlazy.lazy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Builder
    private Article(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static Article create(String title) {
        return Article.builder()
                .id(null)
                .title(title)
                .build();
    }
}
