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
        memoEntityManager.clear();
        MemoEntity result = memoRepository.findById(saved.getId()).orElseThrow();

        // then
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
        todoEntityManager.clear();
        TodoEntity result = todoRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(result.getId()).isEqualTo(saved.getId());
        assertThat(result.getTitle()).isEqualTo(saved.getTitle());
        assertThat(result.getCompleted()).isEqualTo(saved.getCompleted());
        assertThat(result.getCreatedAt()).isEqualTo(saved.getCreatedAt());
        assertThat(result.getLastModifiedAt()).isEqualTo(saved.getLastModifiedAt());
    }
}
