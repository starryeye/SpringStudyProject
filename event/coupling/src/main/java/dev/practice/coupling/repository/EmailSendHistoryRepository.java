package dev.practice.coupling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailSendHistoryRepository extends JpaRepository<EmailSendHistory, Long> {

    // for test
    List<EmailSendHistory> findByTargetMemberId(Long memberId);
}
