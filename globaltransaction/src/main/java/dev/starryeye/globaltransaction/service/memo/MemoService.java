package dev.starryeye.globaltransaction.service.memo;

import dev.starryeye.globaltransaction.domain.memo.MemoEntity;
import dev.starryeye.globaltransaction.domain.memo.MemoRepository;
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
public class MemoService {

    private final MemoRepository memoRepository;
    private final EntityManager memoEntityManager;

    public void createMemo(String title, String content) {

        log.info("transaction active = {}, name = {}",
                TransactionSynchronizationManager.isActualTransactionActive(),
                TransactionSynchronizationManager.getCurrentTransactionName()
        );
        log.info("entity manager = {}", memoEntityManager);

        MemoEntity memo = MemoEntity.create(title, content);

        memoRepository.save(memo);
    }

    public void editContent(Long id, String content) {

        log.info("transaction active = {}, name = {}",
                TransactionSynchronizationManager.isActualTransactionActive(),
                TransactionSynchronizationManager.getCurrentTransactionName()
        );
        log.info("entity manager = {}", memoEntityManager);

        MemoEntity memo = memoRepository.findById(id).orElseThrow();

        log.info("memo persistence state = {}", memoEntityManager.contains(memo)); // 영속 상태

        memo.changeContent(content);
    }

    public void editContentWithWrongTransaction(Long id, String content) {

        log.info("transaction active = {}, name = {}", TransactionSynchronizationManager.isActualTransactionActive(), TransactionSynchronizationManager.getCurrentTransactionName());

        MemoEntity memo = memoRepository.findById(id).orElseThrow();

        log.info("find memo = {}", memo);
        log.info("memo persistence state = {}", memoEntityManager.contains(memo)); // 준영속 상태

        memo.changeContent(content);
    }

    public void throwException() {
        throw new RuntimeException("memo exception!");
    }
}
