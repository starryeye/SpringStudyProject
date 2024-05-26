package dev.practice.restclient.clients.request;

public record Post(
        Integer userId,
        Integer id,
        String title,
        String body
) {
}
