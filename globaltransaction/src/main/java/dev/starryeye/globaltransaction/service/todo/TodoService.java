package dev.starryeye.globaltransaction.service.todo;

import dev.starryeye.globaltransaction.domain.todo.TodoEntity;
import dev.starryeye.globaltransaction.domain.todo.TodoRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Transactional(transactionManager = "jtaTransactionManager")
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final EntityManager todoEntityManager;

    public void createTodo(String title) {

        log.info("transaction active = {}, name = {}",
                TransactionSynchronizationManager.isActualTransactionActive(),
                TransactionSynchronizationManager.getCurrentTransactionName()
        );
        log.info("entity manager = {}", todoEntityManager);

        TodoEntity todo = TodoEntity.create(title);

        todoRepository.save(todo);
    }

    public void updateCompletion(Long id, Boolean completion) {

        log.info("transaction active = {}, name = {}",
                TransactionSynchronizationManager.isActualTransactionActive(),
                TransactionSynchronizationManager.getCurrentTransactionName()
        );
        log.info("entity manager = {}", todoEntityManager);

        TodoEntity todo = todoRepository.findById(id).orElseThrow();

        /**
         * todo, 아래 코드가 동작하지 않음..
         * todoEntityManager 가 사실 memoEntityManager 와 동일한 것에서 부터 추론 시작 해야함
         */
//        log.info("todo persistence state = {}", todoEntityManager.contains(todo)); // 영속 상태

        todo.changeCompleted(completion);
    }

    public void throwException() {
        throw new RuntimeException("todo exception!");
    }
}
