package dev.practice.multipledatasources.facade;

import dev.practice.multipledatasources.service.memo.MemoService;
import dev.practice.multipledatasources.service.todo.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
}
