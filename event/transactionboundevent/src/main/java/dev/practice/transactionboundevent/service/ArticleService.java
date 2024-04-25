package dev.practice.transactionboundevent.service;

import dev.practice.transactionboundevent.repository.Article;
import dev.practice.transactionboundevent.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public Article save(String title, String author, String content) {

        Article article = Article.create(title, author, content);

        articleRepository.save(article);

        log.info("Article saved: {}", article);

        return article;
    }

    @Transactional(readOnly = true)
    public Article getArticle(String title) {
        return articleRepository.findByTitle(title).orElseThrow();
    }

    @Transactional
    public Article changeArticleContent(Long id, String content) {

        Article article = articleRepository.findById(id).orElseThrow();

        article.changeContent(content);

        return article;
    }
}
