package dev.practice.transactionboundevent.event.listener;

import dev.practice.transactionboundevent.event.ArticleCreatedEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Getter
@Slf4j
@Component
public class ArticleEventListener {

    // for test
    private boolean beforeCommitListenerCalled = false;
    private boolean afterCommitListenerCalled = false;
    private boolean afterCompletionListenerCalled = false;
    private boolean afterRollbackListenerCalled = false;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, condition = "#event.success")
    public void articleCreatedEventHandleBeforeCommit(ArticleCreatedEvent event) {
        log.info("[Before Commit] Received ArticleCreated.. event: {}, isSuccess : {}", event.getWhat(), event.isSuccess());
        beforeCommitListenerCalled = true;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, condition = "#event.success")
    public void articleCreatedEventHandleAfterCommit(ArticleCreatedEvent event) {
        log.info("[After Commit] Received ArticleCreated.. event: {}, isSuccess : {}", event.getWhat(), event.isSuccess());
        afterCommitListenerCalled = true;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION, condition = "#event.success")
    public void articleCreatedEventHandleAfterCompletion(ArticleCreatedEvent event) {
        log.info("[After Completion] Received ArticleCreated.. event: {}, isSuccess : {}", event.getWhat(), event.isSuccess());
        afterCompletionListenerCalled = true;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK, condition = "#event.success") // todo, ???
    public void articleCreatedEventHandleAfterRollback(ArticleCreatedEvent event) {
        log.info("[After Rollback] Received ArticleCreated.. event: {}, isSuccess : {}", event.getWhat(), event.isSuccess());
        afterRollbackListenerCalled = true;
    }
}
