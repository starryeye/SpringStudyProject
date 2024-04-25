package dev.practice.transactionboundevent.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String content;

    @Builder
    private Article(Long id, String title, String author, String content) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public static Article create(String title, String author, String content) {
        return Article.builder()
                .id(null)
                .title(title)
                .author(author)
                .content(content)
                .build();
    }

    public void changeContent(String newContent) {
        this.content = newContent;
    }
}
