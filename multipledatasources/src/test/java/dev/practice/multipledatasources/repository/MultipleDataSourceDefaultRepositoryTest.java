package dev.practice.multipledatasources.repository;

import dev.practice.multipledatasources.repository.memo.MemoEntity;
import dev.practice.multipledatasources.repository.memo.MemoRepository;
import dev.practice.multipledatasources.repository.todo.TodoEntity;
import dev.practice.multipledatasources.repository.todo.TodoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class MultipleDataSourceDefaultRepositoryTest {

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private TodoRepository todoRepository;

    @PersistenceContext(unitName = "memoEntityManager")
    private EntityManager memoEntityManager;

    @PersistenceContext(unitName = "todoEntityManager")
    private EntityManager todoEntityManager;

    @AfterEach
    void tearDown() {
        memoRepository.deleteAllInBatch();
        todoRepository.deleteAllInBatch();
    }

    @Test
    void contextLoad() {

        /**
         * @DataJpaTest 로 하면 에러가 나는데, @DataJpaTest 는 기본적으로 H2 .. embedded db 를 기본값으로 한다고 함..
         *
         * 관련 코드
         * @DataJpaTest
         * @AutoConfigureTestDatabase
         * TestDatabaseAutoConfiguration
         *
         * 해결 코드
         * @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
         */
        log.info("memosEntityManager = {}", memoEntityManager.toString());
        log.info("todosEntityManager = {}", todoEntityManager.toString());

        assertThat(memoEntityManager).isNotEqualTo(todoEntityManager);
    }

    @DisplayName("memo 저장이 되어야한다.")
    @Test
    void memoSave() {

        // given
        MemoEntity memo = MemoEntity.builder()
                .title("memo title")
                .content("memo content")
                .build();

        // when
        MemoEntity saved = memoRepository.save(memo);

        // then
        MemoEntity result = memoRepository.findById(saved.getId()).orElseThrow();
        assertThat(result.getId()).isEqualTo(saved.getId());
        assertThat(result.getTitle()).isEqualTo(saved.getTitle());
        assertThat(result.getContent()).isEqualTo(saved.getContent());
        assertThat(result.getCreatedAt()).isEqualTo(saved.getCreatedAt());
        assertThat(result.getLastModifiedAt()).isEqualTo(saved.getLastModifiedAt());
    }

    @DisplayName("todo 저장이 되어야한다.")
    @Test
    void todoSave() {

        // given
        TodoEntity todo = TodoEntity.builder()
                .title("todo title")
                .completed(Boolean.TRUE)
                .build();

        // when
        TodoEntity saved = todoRepository.save(todo);

        // then
        TodoEntity result = todoRepository.findById(saved.getId()).orElseThrow();
        assertThat(result.getId()).isEqualTo(saved.getId());
        assertThat(result.getTitle()).isEqualTo(saved.getTitle());
        assertThat(result.getCompleted()).isEqualTo(saved.getCompleted());
        assertThat(result.getCreatedAt()).isEqualTo(saved.getCreatedAt());
        assertThat(result.getLastModifiedAt()).isEqualTo(saved.getLastModifiedAt());
    }

    @Transactional(transactionManager = "memoTransactionManager")
    @DisplayName("entityManager 정상 동작 해야한다.")
    @Test
    void entityManager() {

        // given
        MemoEntity given = MemoEntity.builder()
                .title("memo title")
                .content("memo content")
                .build();
        memoRepository.save(given);

        // when
        memoEntityManager.clear();
        MemoEntity result = memoRepository.findById(given.getId()).orElseThrow();

        // then
        // em.clear 로 인해 두 엔티티는 동일성 보장이 안됨 (두 엔티티는 준영속)
        assertThat(given.hashCode()).isNotEqualTo(result.hashCode());
    }

    @Transactional(transactionManager = "memoTransactionManager")
    @DisplayName("다른 entityManager 는 작동하지 않아야한다.")
    @Test
    void entityManager2() {

        // given
        MemoEntity given = MemoEntity.builder()
                .title("memo title")
                .content("memo content")
                .build();
        memoRepository.save(given);

        // when
        todoEntityManager.clear(); // todoEntityManager 는 memo Transaction 에서 동작하지 않음
        MemoEntity result = memoRepository.findById(given.getId()).orElseThrow();

        // then
        // 잘못된 em 을 사용하였기 때문에 영속성 컨텍스트가 유지 되어 동일성 보장이 되어버림 (하나의 영속엔티티)
        assertThat(given.hashCode()).isEqualTo(result.hashCode());
    }
}
