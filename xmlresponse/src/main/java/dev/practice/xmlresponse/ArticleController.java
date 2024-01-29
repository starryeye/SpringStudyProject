package dev.practice.xmlresponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {

    @GetMapping("/articles")
    public ResponseEntity<ArticleResponse> get() {

        ArticleResponse response = ArticleResponse.builder()
                .title("title")
                .content("content")
                .build();

        return ResponseEntity.accepted()
//                .header()
                .body(response);
    }
}
