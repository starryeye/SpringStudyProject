package dev.practice.transactionboundevent.service;

import dev.practice.transactionboundevent.event.listener.ArticleEventListener;
import dev.practice.transactionboundevent.repository.Article;
import dev.practice.transactionboundevent.repository.ArticleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleEventListener articleEventListener;

    @AfterEach
    void tearDown() {
        articleRepository.deleteAllInBatch();
    }

    @DisplayName("@TransactionalEventListener 의 로그가 예상한 순서에 맞게 출력되었는가..")
    @Test
    void save() {

        /**
         * IDENTITY 전략이라도 insert 쿼리가 DB 로 전달되는 것은 flush ? 이지 commit 이 아니기 때문에..
         * [ArticleService] 로그가 먼저 찍히고 commit 이 모두 된 후(단순 save 로직이므로 commit 시점에 아무것도 안함)
         * [Before Commit], [After Commit], [After Completion] 순으로 로그가 출력된다.
         */

        // given
        String title = "title";
        String author = "author";
        String content = "content";

        // when
        Article saved = articleService.save(title, author, content);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo(title);
        assertThat(saved.getContent()).isEqualTo(content);
        assertThat(saved.getAuthor()).isEqualTo(author);

        assertThat(articleEventListener.isBeforeCommitListenerCalled()).isTrue();
        assertThat(articleEventListener.isAfterCommitListenerCalled()).isTrue();
        assertThat(articleEventListener.isAfterCompletionListenerCalled()).isTrue();
        assertThat(articleEventListener.isAfterRollbackListenerCalled()).isFalse();
    }

    @DisplayName("@TransactionalEventListener 의 로그가 예상한 순서에 맞게 출력되었는가..")
    @Test
    void saveFail() {

        /**
         * Rollback
         */

        // given
        String title = "title";
        String author = "author";
        String content = "content";

        // when
        // then
        assertThatThrownBy(
                () -> articleService.saveFail(title, author, content)
        ).isInstanceOf(IllegalArgumentException.class);

        List<Article> result = articleService.getArticles(title);
        assertThat(result).isEmpty();

        assertThat(articleEventListener.isBeforeCommitListenerCalled()).isFalse();
        assertThat(articleEventListener.isAfterCommitListenerCalled()).isFalse();
        assertThat(articleEventListener.isAfterCompletionListenerCalled()).isTrue();
        assertThat(articleEventListener.isAfterRollbackListenerCalled()).isTrue();
    }

}