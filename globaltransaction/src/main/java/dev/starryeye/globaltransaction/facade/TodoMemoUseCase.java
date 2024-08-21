package dev.starryeye.globaltransaction.facade;

import dev.starryeye.globaltransaction.service.memo.MemoService;
import dev.starryeye.globaltransaction.service.todo.TodoService;
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

    @Transactional(transactionManager = "jtaTransactionManager")
    public void createTodoMemo(String title, String content) {

        todoService.createTodo(title);
        memoService.createMemo(title, content);
    }

    @Transactional(transactionManager = "jtaTransactionManager")
    public void changeStateTodoMemo(Long todoId, Long memoId, Boolean todoCompletion, String memoContent) {

        /**
         * 동작 함에 있어서 아래 설정 참고함.
         * https://stackoverflow.com/questions/36912251/atomikos-exception-when-transaction-contains-more-than-one-persist
         * - pinGlobalTxToPhysicalConnection=true
         * - spring.jta.atomikos.properties.serial-jta-transactions=false
         */

        todoService.updateCompletion(todoId, todoCompletion);
        memoService.editContent(memoId, memoContent);
    }

    @Transactional(transactionManager = "jtaTransactionManager")
    public void changeStateMemoWithTodoException(Long memoId, String memoContent) {

        memoService.editContent(memoId, memoContent);
        try {
            todoService.throwException(); // exception!
        }catch (RuntimeException e) {
            log.error("todo exception catch 처리", e);
        }
    }

    @Transactional(transactionManager = "jtaTransactionManager")
    public void changeStateMemoWithMemoException(Long memoId, String memoContent) {

        memoService.editContent(memoId, memoContent);
        try {
            memoService.throwException(); // exception!
        }catch (RuntimeException e) {
            log.error("memo exception catch 처리", e);
        }
    }
}
