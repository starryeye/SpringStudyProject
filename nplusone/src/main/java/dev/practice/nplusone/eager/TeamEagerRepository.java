package dev.practice.nplusone.eager;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamEagerRepository extends JpaRepository<TeamEager, Long> {

    List<TeamEager> findByIdIn(List<Long> ids);

    @EntityGraph(attributePaths = "memberEagers")
    List<TeamEager> findEntityGraphByIdIn(List<Long> ids);
}
