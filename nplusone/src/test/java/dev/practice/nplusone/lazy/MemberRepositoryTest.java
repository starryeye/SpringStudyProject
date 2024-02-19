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

    @DisplayName("Lazy 전략에서는 ManyToOne 관계가 있는 엔티티를 조회 하면 연관관계에 있는 엔티티는 조회하지 않고 실제 사용할때 조회된다.")
    @Test
    @Transactional // 지연 로딩 사용하기 위함
    void findAll() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * 1 건의 쿼리가 나감, member 전체 조회
         */
        System.out.println("===============when, 쿼리===============");
        List<Member> result = memberRepository.findAll();
        System.out.println("===============when, 쿼리===============");

        // then
        result.forEach(
                member -> assertThat(persistenceUnitUtil.isLoaded(member.getTeam())).isFalse() // Lazy 전략이라 초기화 안됨
        );

        /**
         * N + 1 문제 발생, Lazy Loading
         * 3 건의 쿼리가 나감
         * member 가 가지고 있는 FK(team pk) 만큼 수행
         */
        System.out.println("===============then, 추가 쿼리 확인===============");

        Set<Team> teams = new HashSet<>();

        result.forEach(
                member -> {
                    member.getTeam().getName(); // 프록시 초기화
                    teams.add(member.getTeam());
                });

        System.out.println("===============then, 추가 쿼리 확인===============");

        System.out.println("result.size() : " + result.size() + ", N : " + teams.size());
    }

    @DisplayName("Entity Graph 를 사용하면 fetch join 으로.. N + 1 문제를 해결할 수 있다.")
    @Test
    void findEntityGraphByIdIn() {

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
                member -> assertThat(persistenceUnitUtil.isLoaded(member.getTeam())).isTrue() // EntityGraph(or 페치 조인) 로 이미 초기화됨
        );

        /**
         * 추가 쿼리 없음 (N + 1 해결, EntityGraph)
         */
        System.out.println("===============then, 추가 쿼리 확인===============");

        result.forEach(
                member -> {
                    member.getTeam().getName(); // 프록시 초기화
                });

        System.out.println("===============then, 추가 쿼리 확인===============");
    }
}