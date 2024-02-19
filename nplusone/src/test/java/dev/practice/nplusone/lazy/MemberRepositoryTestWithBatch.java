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
@ActiveProfiles("batch") // default_batch_fetch_size 적용
public class MemberRepositoryTestWithBatch {

    /**
     * MemberRepositoryTest 에서 발생한 N + 1 문제를 Batch 옵션으로 해결해본다.
     *
     * Batch 옵션은 N 번의 추가 쿼리를 1 회로 줄여주는 효과를 발휘한다.
     */

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("BatchSize 옵션을 통하여 ManyToOne 연관관계 컬렉션 엔티티를 지연 로딩시 BatchSize 만큼 하나의 쿼리로 수행한다. N + 1 문제를 해결한다. 1 + 1 쿼리이다.")
    @Test
    @Transactional
    void findAllByIdIn() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * 1회의 쿼리가 나간다. member 들만 조회
         */
        System.out.println("===============when, 쿼리===============");
        List<Member> result = memberRepository.findAllByIdIn(List.of(1L, 4L, 7L));
        System.out.println("===============when, 쿼리===============");

        // then
        result.forEach(
                member -> assertThat(persistenceUnitUtil.isLoaded(member.getTeam())).isFalse() // Lazy 전략이라 초기화 안됨
        );

        /**
         * batch 를 사용하지 않으면, 아래에서 lazy loading 으로 인해 3번의 쿼리가 나간다.
         * batch 를 사용하여 1회의 쿼리(in query)만 나간다. N + 1 해결
         */
        System.out.println("===============then, 추가 쿼리 확인===============");
        result.forEach(
                member -> member.getTeam().getName() // 프록시 초기화
        );
        System.out.println("===============then, 추가 쿼리 확인===============");
    }
}
