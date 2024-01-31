package dev.practice.xmlresponse;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonRootName(value = "article")
@Getter
public class ArticleResponse {

    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final String nullProperty = null;

    @Builder
    private ArticleResponse(String title, String content, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
