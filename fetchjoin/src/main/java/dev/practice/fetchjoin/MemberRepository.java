package dev.practice.fetchjoin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 엔티티 페치 조인
     */
    @Query(
            value = """
                    select m
                    from Member m
                    join fetch m.team
                    """
    )
    List<Member> findAllBy();
}
