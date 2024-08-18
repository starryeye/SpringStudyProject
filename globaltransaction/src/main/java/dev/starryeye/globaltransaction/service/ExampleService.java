package dev.starryeye.globaltransaction.service;

import dev.starryeye.globaltransaction.domain.MemoRepository;
import dev.starryeye.globaltransaction.domain.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExampleService {

    private final MemoRepository memoRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public void performTransaction() {

    }
}
