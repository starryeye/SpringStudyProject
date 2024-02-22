package dev.practice.batch;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@DataJpaTest // Transactional
class SolveMyDoubts {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("sequence 전략에서는 persist 에 대하여 쓰기 지연이 지원된다.")
    @Test
    void test() {

        // given
        Article article = Article.create("title", "content");

        // when
        Article saved = articleRepository.save(article);

        // then
        log.info("saved id = {}", saved.getId()); // 쓰기 지연으로 인해 insert 쿼리 로그 보다 먼저 찍힌다.
    }

    @DisplayName("10000 건의 데이터를 save")
    @Test
    void save() {

        // given
        // when
        // then
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        IntStream.range(0, 10000)
                .forEach(
                        i -> {
                            Article article = Article.create("title %s".formatted(i), "content");
                            articleRepository.save(article);
                        }
                );

        stopWatch.stop();
        log.info("elapsed time = {}", stopWatch.getTotalTimeMillis()); // 331ms
    }

    @DisplayName("10000 건의 데이터를 saveAll")
    @Test
    void saveAll() {

        /**
         * save 를 10000 번 호출하면, Transactional AOP 처리가 10000 번 이루어진다.
         * saveAll 을 1 번 호출하면, Transactional AOP 처리가 1 번 이루어지고 this.save 가 10000 번 이루어진다.
         * -> 즉, saveAll 을 사용하면 트랜잭션 전파(기존 트랜잭션 참여)에 대한 처리가 한번만 이루어져서 save 보다 성능 우위에 있을 수 있다.
         */

        // given
        // when
        // then
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Article> articles = IntStream.range(0, 10000)
                .mapToObj(i -> Article.create("title %s".formatted(i), "content"))
                .toList();
        articleRepository.saveAll(articles);

        stopWatch.stop();
        log.info("elapsed time = {}", stopWatch.getTotalTimeMillis()); // 187ms
    }
}