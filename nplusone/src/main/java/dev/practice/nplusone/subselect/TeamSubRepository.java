package dev.practice.nplusone.subselect;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamSubRepository extends JpaRepository<TeamSub, Long> {

    List<TeamSub> findByIdGreaterThan(Long id);
}
