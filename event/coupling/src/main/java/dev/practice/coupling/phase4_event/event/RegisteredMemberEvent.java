package dev.practice.coupling.phase4_event.event;

public record RegisteredMemberEvent(
        Long id,
        String name,
        String email
) {

    public static RegisteredMemberEvent create(Long id, String name, String email) {
        return new RegisteredMemberEvent(id, name, email);
    }
}
