package dev.practice.transactionboundevent.event;

import dev.practice.transactionboundevent.common.GenericSpringEvent;

public class ArticleCreatedEvent extends GenericSpringEvent<String> {

    private static final String ARTICLE_CREATED_MESSAGE = "ArticleCreated::%s";

    public ArticleCreatedEvent(String id, boolean isSuccess) {
        super(ARTICLE_CREATED_MESSAGE.formatted(id), isSuccess);
    }
}
