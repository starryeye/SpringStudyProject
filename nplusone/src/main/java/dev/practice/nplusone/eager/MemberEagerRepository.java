package dev.practice.nplusone.eager;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberEagerRepository extends JpaRepository<MemberEager, Long> {

    @Query(value = "select m from MemberEager m where m.id in (:ids)")
    List<MemberEager> findMemberEagerByIdIn(List<Long> ids);

    @EntityGraph(attributePaths = {"teamEager"}) // 참고로 메서드 이름 조회 뿐만 아니라 JPQL 에도 적용할 수 있다.
    List<MemberEager> findEntityGraphByIdIn(List<Long> ids);

    List<MemberEager> findByIdIn(List<Long> ids);
}
