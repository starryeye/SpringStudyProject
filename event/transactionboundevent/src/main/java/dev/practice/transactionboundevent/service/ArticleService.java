package dev.practice.transactionboundevent.service;

import dev.practice.transactionboundevent.event.publisher.ArticleEventPublisher;
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
    private final ArticleEventPublisher articleEventPublisher;

    @Transactional
    public Article save(String title, String author, String content) {

        Article article = Article.create(title, author, content);

        articleRepository.save(article); // 실패하면 다양한 exception 발생함, 여기서는 예제 코드이므로 실패 이벤트는 발행안함
        articleEventPublisher.articleCreatedEventPublish(article.getId()); // 발행 코드의 위치와 관계 없이.. Listener 는 TransactionalEventListener phase 처리 순서에 따라 동작한다.

        log.info("[ArticleService] Article saved : {}", article);

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

    @Transactional
    public Article changeArticleContentFail(Long id, String content) {

        Article article = articleRepository.findById(id).orElseThrow();

        try {
            article.changeContent(content);
            throw new RuntimeException();
        }catch (RuntimeException e) {
            // 여기서 실패 이벤트 던지면.. 받나?
            throw e;
        }
    }
}
