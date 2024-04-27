package dev.practice.transactionboundevent.service;

import dev.practice.transactionboundevent.event.publisher.ArticleEventPublisher;
import dev.practice.transactionboundevent.repository.Article;
import dev.practice.transactionboundevent.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleEventPublisher articleEventPublisher;

    @Transactional
    public Article save(String title, String author, String content) {

        Article article = Article.create(title, author, content);

        articleRepository.save(article); // 실패하면 다양한 exception 발생함, 여기서는 예제 코드이므로 실패 이벤트는 발행안함
        articleEventPublisher.articleCreatedEventPublish(article.getId(), true); // 발행 코드의 위치와 관계 없이.. Listener 는 TransactionalEventListener phase 처리 순서에 따라 동작한다.

        log.info("[ArticleService] Article saved : {}", article);

        return article;
    }

    @Transactional
    public Article saveFail(String title, String author, String content) {

        Article article = Article.create(title, author, content);

        try {
            articleRepository.save(article); // identity 전략이라 DB 로 전송됨
            log.info("[ArticleService] Article saved : {}", article);

            throw new IllegalArgumentException(); // 모종의 이유로 실패.. -> rollback 해야함

        }catch (RuntimeException e) {
            articleEventPublisher.articleCreatedEventPublish(article.getId(), false);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Article> getArticles(String title) {
        return articleRepository.findByTitle(title);
    }

    @Transactional
    public Article changeArticleContent(Long id, String content) {

        Article article = articleRepository.findById(id).orElseThrow();

        article.changeContent(content);

        return article;
    }
}
