package dev.practice.nplusone.lazy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("batch")
public class TeamRepositoryTestWithBatch {

    /**
     * TeamRepositoryTest 에서 발생한 N + 1 문제를 Batch 옵션으로 해결해본다.
     *
     * Batch 옵션은 N 번의 추가 쿼리를 1 회로 줄여주는 효과를 발휘한다.
     */

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("Lazy 전략에서는 엔티티를 조회 하면 연관관계인 컬렉션 엔티티는 조회하지 않고 실제 사용할때 조회된다.")
    @Test
    @Transactional
    void findAll() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * 쿼리 1 건 수행
         */
        System.out.println("===============when, 쿼리===============");
        List<Team> result = teamRepository.findAll();
        System.out.println("===============when, 쿼리===============");

        // then
        /**
         * TeamRepositoryTest 에서는 총 4 건의 쿼리가 나갔다. (Lazy loading 으로 인한 추가 쿼리는 3 회)
         *
         * batch size 옵션을 통해 지연 로딩 추가 쿼리 3 회를 1 회로 줄였다.
         */
        System.out.println("===============then, 추가 쿼리 확인===============");
        result.forEach(
                team -> {

                    // 최초 1회는 false 이고 나머지는 true 이다.
//                    assertThat(persistenceUnitUtil.isLoaded(team.getMembers())).isFalse(); // Lazy 전략이라 초기화 안됨


                    /**
                     * 주의 사항!
                     *
                     * teamRepository.findAll() 의 결과는 List<Team> 이다..
                     * 여기서 하나의 team 에 연관된 컬렉션 엔티티중 하나의 member 만 초기화 해도..
                     * List<Team> 에 담긴 모든 컬렉션 엔티티가 모두 초기화 된다.. (1 회의 쿼리로..)
                     */
                    team.getMembers().forEach(
                            Member::getName
                    );
                }
        );
        System.out.println("===============then, 추가 쿼리 확인===============");
    }
}
