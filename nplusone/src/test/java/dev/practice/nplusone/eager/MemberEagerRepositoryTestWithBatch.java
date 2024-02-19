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
@ActiveProfiles("batch") // default_batch_fetch_size 적용
public class MemberEagerRepositoryTestWithBatch {

    /**
     * MemberEagerRepositoryTest 에서 발생한 N + 1 문제를 Batch 옵션으로 해결해본다.
     *
     * Batch 옵션은 N 번의 추가 쿼리를 1 회로 줄여주는 효과를 발휘한다.
     */

    @Autowired
    private MemberEagerRepository memberEagerRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("Eager 전략에서 JPQL 을 사용하여 엔티티를 조회하면 N + 1 추가 쿼리가 발생할 수 있는데 이를 batch 옵션으로 해결한다.")
    @Test
    void findMemberEagerBy() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * MemberEagerRepositoryTest 에서는 총 3 건의 쿼리가 나갔다. (추가 쿼리는 2 회)
         *
         * batch size 옵션을 통해 추가 쿼리 2 회를 1 회로 줄였다.
         */
        System.out.println("===============when, 쿼리===============");
        List<MemberEager> result = memberEagerRepository.findMemberEagerByIdIn(List.of(1L, 4L, 5L));
        System.out.println("===============when, 쿼리===============");

        // then
        /**
         * 여기서는 위에서 이미 조회가 다 이루어 져서 프록시 초기화가 되어있는 결과가 나옴
         */
        System.out.println("===============then, 추가 쿼리 확인===============");
        result.forEach(
                memberEager -> {
                    assertThat(persistenceUnitUtil.isLoaded(memberEager.getTeamEager())).isTrue(); // Eager 전략으로 이미 초기화됨
                    memberEager.getTeamEager().getName();
                }
        );
        System.out.println("===============then, 추가 쿼리 확인===============");
    }

    @DisplayName("Eager 전략인데 메서드 이름 조회를 하면... N + 1 문제가 나타나는데 이를 batch 옵션으로 해결해본다.")
    @Test
    void findByIdIn() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * MemberEagerRepositoryTest 에서는 총 4 건의 쿼리가 나갔다. (추가 쿼리는 3 회)
         *
         * batch size 옵션을 통해 추가 쿼리 3 회를 1 회로 줄였다.
         */
        System.out.println("===============when, 쿼리===============");
        List<MemberEager> result = memberEagerRepository.findByIdIn(List.of(1L, 4L, 7L));
        System.out.println("===============when, 쿼리===============");

        // then
        /**
         * 여기서는 위에서 이미 조회가 다 이루어 져서 프록시 초기화가 되어있는 결과가 나옴
         */
        result.forEach(
                member -> assertThat(persistenceUnitUtil.isLoaded(member.getTeamEager())).isTrue() // Eager 전략으로 이미 초기화됨
        );

        System.out.println("===============then, 추가 쿼리 확인===============");

        result.forEach(
                member -> {
                    member.getTeamEager().getName(); // 프록시 초기화
                });

        System.out.println("===============then, 추가 쿼리 확인===============");
    }
}
