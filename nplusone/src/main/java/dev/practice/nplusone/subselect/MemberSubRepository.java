package dev.practice.nplusone.subselect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberSubRepository extends JpaRepository<MemberSub, Long> {

    @Query(value = "select m from MemberSub m where m.id = :id")
    Optional<MemberSub> findMemberSubById(Long id);
}
