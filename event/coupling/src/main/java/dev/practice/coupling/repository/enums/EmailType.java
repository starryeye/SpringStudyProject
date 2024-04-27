package dev.practice.coupling.repository.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailType {

    MEMBER_REGISTER_SUCCESS("회원가입성공");

    private final String text;
}
