package dev.practice.coupling.dto;

import dev.practice.coupling.repository.Member;

public record RegisterMemberResponse(
        Long memberId
) {

    public static RegisterMemberResponse of(Member member) {
        return new RegisterMemberResponse(member.getId());
    }
}
