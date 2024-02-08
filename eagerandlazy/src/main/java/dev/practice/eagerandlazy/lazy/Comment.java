package dev.practice.eagerandlazy.lazy;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @Builder
    private Comment(Long id, String content, Article article) {
        this.id = id;
        this.content = content;
        this.article = article;
    }

    public static Comment create(String content, Article article) {
        return Comment.builder()
                .id(null)
                .content(content)
                .article(article)
                .build();
    }
}
