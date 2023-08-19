package dev.practice.nplusone.lazy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("ManyToOne 관계에서 findAll 을 하여 조회 하면 조회 된 만큼 추가 쿼리가 나갈 수 있다.")
    @Test
    @Transactional
    void findAll_And_N_Plus_One_Problem() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        System.out.println("===============when, 쿼리===============");
        List<Member> result = memberRepository.findAll();
        System.out.println("===============when, 쿼리===============");

        // then
        /**
         * 1 건의 쿼리가 나감
         */
        result.forEach(
                member -> assertThat(persistenceUnitUtil.isLoaded(member.getTeam())).isFalse()
        );

        /**
         * 3 건의 쿼리가 나감 (N + 1 문제, Lazy Loading)
         */
        System.out.println("===============then, Lazy Loading 으로 인한 추가 쿼리 확인===============");

        Set<Team> teams = new HashSet<>();

        result.forEach(
                member -> {
                    member.getTeam().getName(); // 프록시 초기화
                    teams.add(member.getTeam());
                });

        System.out.println("===============then, Lazy Loading 으로 인한 추가 쿼리 확인===============");

        System.out.println("result.size() : " + result.size() + ", N : " + teams.size());
    }

    @DisplayName("Entity Graph 를 사용하면 fetch join 으로.. N + 1 문제를 해결할 수 있다.")
    @Test
    void findEntityGraphByIdIn_And_N_Plus_One_Problem_Solved() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * 1 건의 쿼리가 나감
         */
        System.out.println("===============when, 쿼리===============");
        List<Member> result = memberRepository.findEntityGraphByIdIn(List.of(1L, 2L, 3L));
        System.out.println("===============when, 쿼리===============");

        // then
        result.forEach(
                member -> assertThat(persistenceUnitUtil.isLoaded(member.getTeam())).isTrue()
        );

        /**
         * 추가 쿼리 없음 (N + 1 해결, EntityGraph)
         */
        System.out.println("===============then, Lazy Loading 으로 인한 추가 쿼리 확인===============");

        result.forEach(
                member -> {
                    member.getTeam().getName(); // 프록시 초기화
                });

        System.out.println("===============then, Lazy Loading 으로 인한 추가 쿼리 확인===============");
    }
}