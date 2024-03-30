package dev.practice.multipledatasources.service.todo;

import dev.practice.multipledatasources.repository.todo.TodoEntity;
import dev.practice.multipledatasources.repository.todo.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(transactionManager = "todoTransactionManager")
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public void createTodo(String title) {

        TodoEntity todo = TodoEntity.create(title);

        todoRepository.save(todo);
    }

    public void updateCompletion(Long id, Boolean completion) {

        TodoEntity todo = todoRepository.findById(id).orElseThrow();

        todo.changeCompleted(completion);
    }
}
