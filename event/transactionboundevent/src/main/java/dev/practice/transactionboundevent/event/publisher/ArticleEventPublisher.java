package dev.practice.transactionboundevent.event.publisher;

import dev.practice.transactionboundevent.event.ArticleCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void articleCreatedEventPublish(Long articleId, boolean isSuccess) {

        log.info("articleCreatedEvent Publishing.. id: {}", articleId);

        ArticleCreatedEvent event = new ArticleCreatedEvent(String.valueOf(articleId), isSuccess);

        eventPublisher.publishEvent(event);
    }
}
