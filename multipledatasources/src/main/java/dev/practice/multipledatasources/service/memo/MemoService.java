package dev.practice.multipledatasources.service.memo;

import dev.practice.multipledatasources.repository.memo.MemoEntity;
import dev.practice.multipledatasources.repository.memo.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(transactionManager = "memoTransactionManager")
@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;

    public void createMemo(String title, String content) {

        MemoEntity.create(title, content);
    }
}
