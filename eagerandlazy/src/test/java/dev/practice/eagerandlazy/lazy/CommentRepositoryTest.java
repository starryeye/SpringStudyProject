package dev.practice.eagerandlazy.lazy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        articleRepository.deleteAllInBatch();
    }

    @DisplayName("ManyToOne 에서 Lazy, 지연 로딩 전략을 사용하면 연관관계 객체를 실제로 접근할때 프록시 초기화가 이루어진다.")
    @Test
    void test() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        Article article = Article.create("article 1");

        articleRepository.save(article);

        Comment comment1 = Comment.create("comment 1", article);
        Comment comment2 = Comment.create("comment 2", article);
        Comment comment3 = Comment.create("comment 3", article);

        commentRepository.saveAll(List.of(comment1, comment2, comment3));

        // when
        log.info("------------------------before when---------------------------");
        Comment result = commentRepository.findById(comment1.getId()).orElseThrow();
        log.info("------------------------after when---------------------------");

        // then
        assertThat(persistenceUnitUtil.isLoaded(result.getArticle())).isFalse();
    }
}