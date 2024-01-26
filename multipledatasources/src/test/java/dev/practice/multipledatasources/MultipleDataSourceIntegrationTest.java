package dev.practice.multipledatasources;

import dev.practice.multipledatasources.repository.memo.MemoEntity;
import dev.practice.multipledatasources.repository.memo.MemoRepository;
import dev.practice.multipledatasources.repository.todo.TodoEntity;
import dev.practice.multipledatasources.repository.todo.TodoRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
public class MultipleDataSourceIntegrationTest {

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private EntityManager memosEntityManager;

    @Autowired
    private EntityManager todosEntityManager;

    @Test
    void contextLoad() {

        log.info("memosEntityManager = {}", memosEntityManager.toString());
        log.info("todosEntityManager = {}", todosEntityManager.toString());
    }

    @DisplayName("memo 저장이 되어야한다.")
    @Test
    void memoSave() {

        // given
        MemoEntity memo = MemoEntity.create("memo title", "memo content");

        // when
        MemoEntity saved = memoRepository.save(memo);
        memosEntityManager.clear();
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
        TodoEntity todo = TodoEntity.create("todo title", Boolean.TRUE);

        // when
        TodoEntity saved = todoRepository.save(todo);
        todosEntityManager.clear();
        TodoEntity result = todoRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(result.getId()).isEqualTo(saved.getId());
        assertThat(result.getTitle()).isEqualTo(saved.getTitle());
        assertThat(result.getCompleted()).isEqualTo(saved.getCompleted());
        assertThat(result.getCreatedAt()).isEqualTo(saved.getCreatedAt());
        assertThat(result.getLastModifiedAt()).isEqualTo(saved.getLastModifiedAt());
    }
}
