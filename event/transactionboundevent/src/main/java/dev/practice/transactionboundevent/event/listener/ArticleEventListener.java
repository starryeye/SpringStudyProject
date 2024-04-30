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

    /**
     * @TransactionalEventListener
     * - before commit : commit DB 로 전송 이전
     * - after commit : commit DB 로 전송 이후
     * - after completion : commit or rollback 전송 이후
     * - after rollback : rollback DB 로 전송 이후
     *
     * Listener 에서는 기존 @Transactional 에 참여를 못하는 듯하다.. (다시 서치해보기)
     * 명시적으로 @Transactional(REQUIRED_NEW) 로 해주자..
     * phase BEFORE_COMMIT 에서는..?
     */

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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void articleCreatedEventHandleAfterCompletion(ArticleCreatedEvent event) {
        log.info("[After Completion] Received ArticleCreated.. event: {}, isSuccess : {}", event.getWhat(), event.isSuccess());
        afterCompletionListenerCalled = true;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK, condition = "#event.success == false")
    public void articleCreatedEventHandleAfterRollback(ArticleCreatedEvent event) {
        log.info("[After Rollback] Received ArticleCreated.. event: {}, isSuccess : {}", event.getWhat(), event.isSuccess());
        afterRollbackListenerCalled = true;
    }

    // for test
    public void calledStatusInit() {
        this.beforeCommitListenerCalled = false;
        this.afterCommitListenerCalled = false;
        this.afterCompletionListenerCalled = false;
        this.afterRollbackListenerCalled = false;
    }
}
