package dev.practice.batch;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.IntStream;

@ActiveProfiles("batch") // batch 적용
@DataJpaTest
class ArticleRepositoryTestWithBatch {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        articleRepository.deleteAllInBatch();
    }

    //todo batch+saveAll, local+saveAll

    @DisplayName("sequence + batch + insert")
    @Test
    @Rollback(value = false) // 쓰기 지연 확인을 위함
    void test() {

        // given
        // when
        // then
        List<Article> articles = IntStream.range(0, 1000)
                .mapToObj(i -> Article.create("title %s".formatted(i), "content"))
                .toList();

        articleRepository.saveAll(articles);
    }

}