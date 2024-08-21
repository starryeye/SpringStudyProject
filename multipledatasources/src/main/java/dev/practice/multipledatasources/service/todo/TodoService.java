package dev.practice.multipledatasources.service.todo;

import dev.practice.multipledatasources.repository.todo.TodoEntity;
import dev.practice.multipledatasources.repository.todo.TodoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Transactional(transactionManager = "todoTransactionManager")
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    @PersistenceContext(unitName = "todoPersistenceUnit") // @Autowired 로 할 경우 Primary entityManager 가 주입되므로 주의!
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

        log.info("todo persistence state = {}", todoEntityManager.contains(todo)); // 영속 상태

        todo.changeCompleted(completion);
    }

    public void throwException() {
        throw new RuntimeException("todo exception!");
    }
}
