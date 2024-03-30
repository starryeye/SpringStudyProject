package dev.practice.multipledatasources.facade;

import dev.practice.multipledatasources.service.memo.MemoService;
import dev.practice.multipledatasources.service.todo.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodoMemoUseCase {

    private final TodoService todoService;
    private final MemoService memoService;

    public void createTodoMemo(String title, String content) {

        todoService.createTodo(title);
        memoService.createMemo(title, content);
    }

    @Transactional(transactionManager = "memoTransactionManager")
    public void createTodoMemoWithMemoTransaction(String title, String content) {

        todoService.createTodo(title);
        memoService.createMemo(title, content);
    }

    @Transactional(transactionManager = "memoTransactionManager")
    public void changeStateTodoMemoWithMemoTransaction(Long todoId, Long memoId, Boolean todoCompletion, String memoContent) {

        todoService.updateCompletion(todoId, todoCompletion);
        memoService.editContent(memoId, memoContent);
    }

    @Transactional(transactionManager = "memoTransactionManager")
    public void changeStateMemoWithTodoException(Long memoId, String memoContent) {

        memoService.editContent(memoId, memoContent);
        try {
            todoService.throwException(); // exception!
        }catch (RuntimeException e) {
            log.error("todo exception catch 처리", e);
        }
    }

    @Transactional(transactionManager = "memoTransactionManager")
    public void changeStateMemoWithMemoException(Long memoId, String memoContent) {

        memoService.editContent(memoId, memoContent);
        try {
            memoService.throwException(); // exception!
        }catch (RuntimeException e) {
            log.error("memo exception catch 처리", e);
        }
    }
}
