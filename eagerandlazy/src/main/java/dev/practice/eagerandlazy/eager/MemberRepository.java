package dev.practice.eagerandlazy.eager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(
            value = """
                    select m
                    from Member m
                    join m.team
                    where m.id = :id
                    """
    )
    Optional<Member> findMemberByIdWithInnerJoin(@Param("id") Long id); // join 은 inner join 이다. @JoinColumn(nullable = false) 과 상관 없다.

}
