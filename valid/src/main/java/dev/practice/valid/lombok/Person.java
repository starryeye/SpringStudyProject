package dev.practice.valid.lombok;

import lombok.Builder;
import lombok.NonNull;

public class Person {

    @NonNull // lombok
    private final String firstName;

    private final String lastName;
    private final Integer age;

    @Builder
    private Person(String firstName, String lastName, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}
