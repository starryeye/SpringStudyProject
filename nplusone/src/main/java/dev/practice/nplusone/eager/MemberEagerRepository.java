package dev.practice.nplusone.eager;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberEagerRepository extends JpaRepository<MemberEager, Long> {

    @Query(value = "select m from MemberEager m where m.id = :id")
    Optional<MemberEager> findMemberEagerBy(Long id);

    @EntityGraph(attributePaths = {"teamEager"})
    List<MemberEager> findEntityGraphByIdIn(List<Long> ids);
}
