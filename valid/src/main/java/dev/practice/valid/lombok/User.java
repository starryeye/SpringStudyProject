package dev.practice.valid.lombok;

import lombok.NonNull;

public record User(
        @NonNull // lombok
        String firstName,
        String lastName,
        Integer age
) {
}
