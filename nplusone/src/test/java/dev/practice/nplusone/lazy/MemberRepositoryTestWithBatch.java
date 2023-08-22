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
public class MemberRepositoryTestWithBatch {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("BatchSize 옵션을 통하여 ManyToOne N + 1 문제를 해결한다.")
    @Test
    @Transactional
    void findAllByIdIn_With_Batch_And_N_Plus_One_Problem() {

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
                member -> assertThat(persistenceUnitUtil.isLoaded(member.getTeam())).isFalse()
        );

        /**
         * batch 를 사용하지 않으면, 아래에서 lazy loading 으로 인해 3번의 쿼리가 나간다.
         * batch 를 사용하여 1회의 쿼리(in query)만 나간다. N + 1 해결
         */
        System.out.println("===============then, 추가 쿼리 확인===============");
        result.forEach(
                member -> member.getTeam().getName()
        );
        System.out.println("===============then, 추가 쿼리 확인===============");
    }
}
