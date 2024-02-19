package dev.practice.nplusone.eager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("batch")
public class TeamEagerRepositoryTestWithBatch {

    /**
     * TeamEagerRepositoryTest 에서 발생한 N + 1 문제를 Batch 옵션으로 해결해본다.
     *
     * Batch 옵션은 N 번의 추가 쿼리를 1 회로 줄여주는 효과를 발휘한다.
     */

    @Autowired
    private TeamEagerRepository teamEagerRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("XXXToMany 에서 Eager 전략을 사용하고 전체 엔티티 조회를 하면 N + 1 문제가 나타날 수 있는데 batch 옵션으로 해결해본다.")
    @Test
    void findAll() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * TeamEagerRepositoryTest 에서는 총 4 건의 쿼리가 나갔다. (추가 쿼리는 3 회)
         *
         * batch size 옵션을 통해 추가 쿼리 3 회를 1 회로 줄였다.
         */
        System.out.println("===============when, 쿼리===============");
        List<TeamEager> result = teamEagerRepository.findAll();
        System.out.println("===============when, 쿼리===============");

        // then
        /**
         * Eager 전략이라서 when 단계에서 추가 쿼리를 수행하였으므로 then 에서는 추가 쿼리 없다.
         */
        System.out.println("===============then, 추가 쿼리 확인===============");
        result.forEach(
                teamEager -> {
                    assertThat(persistenceUnitUtil.isLoaded(teamEager.getMemberEagers())).isTrue(); // Eager 전략으로 이미 초기화됨

                    teamEager.getMemberEagers().forEach(
                            MemberEager::getName
                    );
                }
        );
        System.out.println("===============then, 추가 쿼리 확인===============");
    }

    @DisplayName("Eager 전략인데 메서드 이름 조회를 하면... N + 1 문제가 나타나는데 batch 옵션으로 해결해본다.")
    @Test
    void findByIdIn() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * TeamEagerRepositoryTest 에서는 총 4 건의 쿼리가 나갔다. (추가 쿼리는 3 회)
         *
         * batch size 옵션을 통해 추가 쿼리 3 회를 1 회로 줄였다.
         */
        System.out.println("===============when, 쿼리===============");
        List<TeamEager> result = teamEagerRepository.findByIdIn(List.of(1L, 2L, 3L));
        System.out.println("===============when, 쿼리===============");

        // then
        /**
         * Eager 전략이라서 when 단계에서 추가 쿼리를 수행하였으므로 then 에서는 추가 쿼리 없다.
         */
        System.out.println("===============then, 추가 쿼리 확인===============");
        result.forEach(
                teamEager -> {
                    assertThat(persistenceUnitUtil.isLoaded(teamEager.getMemberEagers())).isTrue(); // Eager 전략으로 이미 초기화됨

                    teamEager.getMemberEagers().forEach(
                            MemberEager::getName
                    );
                }
        );
        System.out.println("===============then, 추가 쿼리 확인===============");
    }
}
