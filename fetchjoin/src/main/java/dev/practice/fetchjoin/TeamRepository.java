package dev.practice.fetchjoin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * 컬렉션 페치 조인
     */
    @Query(
            value = """
                    select t
                    from Team t
                    join fetch t.members
                    where t.name = :name
                    """
    )
    List<Team> findAllByName(String name);
}
