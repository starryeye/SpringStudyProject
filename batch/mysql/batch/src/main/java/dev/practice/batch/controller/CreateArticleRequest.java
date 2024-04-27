package dev.practice.batch.controller;

public record CreateArticleRequest(
        String title,
        String content
) {
}
