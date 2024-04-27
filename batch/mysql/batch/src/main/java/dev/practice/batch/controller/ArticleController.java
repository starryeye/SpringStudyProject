package dev.practice.batch.controller;

import dev.practice.batch.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/api/v1/save-fail")
    public Long saveFail(@RequestBody CreateArticleRequest request) {

        return articleService.saveFail(request.title(), request.content());
    }
}
