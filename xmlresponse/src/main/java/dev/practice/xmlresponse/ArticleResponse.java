package dev.practice.xmlresponse;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import lombok.Getter;

@JsonRootName(value = "article")
@Getter
public class ArticleResponse {

    private final String title;
    private final String content;

    @Builder
    private ArticleResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
