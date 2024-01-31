package dev.practice.xmlresponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class ArticleController {

    @GetMapping("/articles")
    public ResponseEntity<ArticleResponse> get() {

        ArticleResponse response = ArticleResponse.builder()
                .title("title")
                .content("content")
                .createdAt(LocalDateTime.of(2024, 1, 31, 21, 11))
                .build();

        return ResponseEntity.accepted()
//                .header()
                .body(response);
    }
}
