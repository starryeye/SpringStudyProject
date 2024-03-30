package dev.practice.multipledatasources.facade;

import dev.practice.multipledatasources.repository.memo.MemoEntity;
import dev.practice.multipledatasources.repository.memo.MemoRepository;
import dev.practice.multipledatasources.repository.todo.TodoEntity;
import dev.practice.multipledatasources.repository.todo.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TodoMemoUseCaseTest {

    @Autowired
    private TodoMemoUseCase todoMemoUseCase;

    @Autowired
    private MemoRepository memoRepository;
    @Autowired
    private TodoRepository todoRepository;

    @AfterEach
    void tearDown() {
        todoRepository.deleteAllInBatch();
        memoRepository.deleteAllInBatch();
    }

    @DisplayName("todoService 상위인 todoMemoUseCase::createTodoMemoWithMemoTransaction 에 memoTransaction 이 걸려있어도 정상 저장됨")
    @Test
    void createTodoMemoWithMemoTransaction() {

        // given
        String givenTitle = "test title";
        String givenContent = "test content";

        // when
        todoMemoUseCase.createTodoMemoWithMemoTransaction(givenTitle, givenContent);

        // then
        TodoEntity todoResult = todoRepository.findByTitle(givenTitle).get(0);
        MemoEntity memoResult = memoRepository.findByTitle(givenTitle).get(0);
        assertThat(todoResult.getTitle()).isEqualTo(givenTitle);
        assertThat(memoResult.getTitle()).isEqualTo(givenTitle);
    }

    @DisplayName("when 주석 참고")
    @Test
    void test() {

        // given
        TodoEntity givenTodo = TodoEntity.builder()
                .title("todo title")
                .completed(Boolean.FALSE)
                .build();
        todoRepository.save(givenTodo);

        MemoEntity givenMemo = MemoEntity.builder()
                .title("memo title")
                .content("before change")
                .build();
        memoRepository.save(givenMemo);

        // when
        /**
         * todoMemoUseCase::changeStateTodoMemoWithMemoTransaction 에는 memoTransactionManager 가 걸려있고
         * 호출 메서드에는 각각 도메인에 맞는 트랜잭션이 정상적으로 적용이 되어있는 상황이다.
         *
         * 모두 정상적으로 변경 감지가 이루어진다.
         */
        todoMemoUseCase.changeStateTodoMemoWithMemoTransaction(
                givenTodo.getId(),
                givenMemo.getId(),
                Boolean.TRUE,
                "after change"
        );

        // then
        TodoEntity resultTodo = todoRepository.findById(givenTodo.getId()).orElseThrow();
        MemoEntity resultMemo = memoRepository.findById(givenMemo.getId()).orElseThrow();

        assertThat(resultTodo.getCompleted()).isTrue();
        assertThat(resultMemo.getContent()).isEqualTo("after change");
    }
}