package dev.starryeye.globaltransaction.facade;

import dev.starryeye.globaltransaction.domain.memo.MemoEntity;
import dev.starryeye.globaltransaction.domain.memo.MemoRepository;
import dev.starryeye.globaltransaction.domain.todo.TodoEntity;
import dev.starryeye.globaltransaction.domain.todo.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @DisplayName("글로벌 트랜잭션을 사용할 수 있다.")
    @Test
    void createTodoMemo() {

        // given
        String givenTitle = "test title";
        String givenContent = "test content";

        // when
        todoMemoUseCase.createTodoMemo(givenTitle, givenContent);

        // then
        TodoEntity todoResult = todoRepository.findByTitle(givenTitle).get(0);
        MemoEntity memoResult = memoRepository.findByTitle(givenTitle).get(0);
        assertThat(todoResult.getTitle()).isEqualTo(givenTitle);
        assertThat(memoResult.getTitle()).isEqualTo(givenTitle);
    }

    @DisplayName("글로벌 트랜잭션을 이용하여도 JPA 변경감지를 사용할 수 있다.")
    @Test
    void changeStateTodoMemo() {

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
        todoMemoUseCase.changeStateTodoMemo(
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

    @DisplayName("글로벌 트랜잭션을 사용하여도 트랜잭션 전파는 정상 동작한다. 1")
    @Test
    void changeStateMemoWithTodoException() {

        // given
        MemoEntity given = MemoEntity.builder()
                .title("memo title")
                .content("before content")
                .build();
        memoRepository.save(given);

        // when
        /**
         * 트랜잭션 전파(기본값)에 의해 롤백이 되고.. UnexpectedRollbackException 이 발생함
         */
        assertThatThrownBy(
                () -> todoMemoUseCase.changeStateMemoWithTodoException(given.getId(), "after content")
        ).isInstanceOf(UnexpectedRollbackException.class);

        // then
        MemoEntity result = memoRepository.findById(given.getId()).orElseThrow();
        assertThat(result.getContent()).isEqualTo("before content");
    }

    @DisplayName("글로벌 트랜잭션을 사용하여도 트랜잭션 전파는 정상 동작한다. 2")
    @Test
    void changeStateMemoWithMemoException() {

        // given
        MemoEntity given = MemoEntity.builder()
                .title("memo title")
                .content("before content")
                .build();
        memoRepository.save(given);

        // when
        /**
         * 트랜잭션 전파(기본값)에 의해 롤백이 되고.. UnexpectedRollbackException 이 발생함
         */
        assertThatThrownBy(
                () -> todoMemoUseCase.changeStateMemoWithMemoException(given.getId(), "after content")
        ).isInstanceOf(UnexpectedRollbackException.class);


        // then
        MemoEntity result = memoRepository.findById(given.getId()).orElseThrow();
        assertThat(result.getContent()).isEqualTo("before content");
    }
}