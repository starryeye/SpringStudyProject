package dev.practice.valid.validator;

import jakarta.validation.constraints.NotNull;

public record User(
        @NotNull // jakarta.validation
        String firstName,
        @NotNull // jakarta.validation
        String lastName,
        Integer age
) {
}
