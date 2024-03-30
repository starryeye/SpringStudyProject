package dev.practice.multipledatasources.service.memo;

import dev.practice.multipledatasources.repository.memo.MemoEntity;
import dev.practice.multipledatasources.repository.memo.MemoRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Transactional(transactionManager = "memoTransactionManager")
@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final EntityManager memoEntityManager;

    public void createMemo(String title, String content) {

        MemoEntity memo = MemoEntity.create(title, content);

        memoRepository.save(memo);
    }

    public void editContent(Long id, String content) {

        log.info("transaction active = {}", TransactionSynchronizationManager.isActualTransactionActive());

        MemoEntity memo = memoRepository.findById(id).orElseThrow();

        log.info("memo persistence state = {}", memoEntityManager.contains(memo)); // 영속 상태

        memo.changeContent(content);
    }

    @Transactional(transactionManager = "todoTransactionManager")
    public void editContentWithWrongTransaction(Long id, String content) {

        log.info("transaction active = {}", TransactionSynchronizationManager.isActualTransactionActive());

        MemoEntity memo = memoRepository.findById(id).orElseThrow();

        log.info("find memo = {}", memo);
        log.info("memo persistence state = {}", memoEntityManager.contains(memo)); // 준영속 상태

        memo.changeContent(content);
    }
}
