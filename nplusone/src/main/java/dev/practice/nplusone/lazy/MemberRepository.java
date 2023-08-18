package dev.practice.nplusone.lazy;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByIdIn(List<Long> ids);
}
