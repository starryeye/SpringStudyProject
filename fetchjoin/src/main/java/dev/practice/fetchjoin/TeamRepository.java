package dev.practice.fetchjoin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * 컬렉션 엔티티 페치 조인
     *
     * hibernate 6 부터 join fetch 사용하면 자동 distinct 적용 된다.
     *
     * distinct 는 실제 native Query 에도 적용되지만, JPA 자체의 distinct 의미도 있다.
     * native Query 에서의 distinct 는 모든 데이터가 동일한 row 를 중복으로 본다.
     * JPA 에서는 "select distinct t ~" 로 하면, 중복된 team 엔티티를 하나로 걸러내준다.
     */
    @Query(
            value = """
                    select t
                    from Team t
                    join fetch t.members
                    where t.name = :name
                    """
    )
    List<Team> findAllByNameWithFetchJoin(@Param("name") String teamName);
}
