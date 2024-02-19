package dev.practice.nplusone.eager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberEagerRepositoryTest {

    @Autowired
    private MemberEagerRepository memberEagerRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("Eager 전략에서는 ManyToOne 관계가 있는 엔티티를 조회 하면 연관관계에 있는 엔티티도 함께 조인으로 조회 되어 추가 쿼리가 안나간다.")
    @Test
    void findAll() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * 1 건의 쿼리가 나감
         */
        System.out.println("===============when, 쿼리===============");
        List<MemberEager> result = memberEagerRepository.findAll();
        System.out.println("===============when, 쿼리===============");

        // then
        result.forEach(
                member -> assertThat(persistenceUnitUtil.isLoaded(member.getTeamEager())).isTrue() // Eager 전략으로 이미 초기화됨
        );

        /**
         * 추가 쿼리 없음 (N + 1 문제 해결, Eager 전략)
         */
        System.out.println("===============then, 추가 쿼리 확인===============");

        Set<TeamEager> teams = new HashSet<>();

        result.forEach(
                member -> {
                    member.getTeamEager().getName(); // 프록시 초기화
                    teams.add(member.getTeamEager());
                });

        System.out.println("===============then, 추가 쿼리 확인===============");
    }

    @DisplayName("Eager 전략에서 JPQL 을 사용하여 엔티티를 조회하면 N + 1 추가 쿼리가 발생할 수 있다.")
    @Test
    void findMemberEagerBy() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * 3 건의 쿼리가 나간다!! (N + 1 문제, Eager 전략이지만 fetch join 없는 JPQL)
         * member 조회 쿼리 1 건 (in 절, id = 1, 4, 5)
         * + member 의 연관관계인 team 엔티티 조회 2 건 (member 가 3 개인데 4, 5 에 대해 동일한 team 엔티티 이므로 1차 캐시 이용됨)
         *
         *
         * "select m from MemberEager m where m.id in (:ids)"
         * 위 JPQL(그냥 해당 엔티티들만 조회) 을 우선적으로 수행하고..
         * 이후 Eager 전략이므로 연관관계에 있는 엔티티에 대한 추가 쿼리가 수행된다.
         *
         * 결론
         * Eager 전략을 Entity 에 선언 하였더라도 JPQL 을 사용할 경우, 의도 대로 한방 쿼리를 수행하기 위해서는..
         * JPQL 에서 잊지 않고 fetch join 을 해줘야한다.
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

    @DisplayName("Eager 전략인데 메서드 이름 조회를 하면... N + 1 문제가 나타난다.")
    @Test
    void findByIdIn() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * 쿼리 4 건 나감 (N + 1 문제)
         * member 조회 쿼리 1 건 (in 절, id = 1, 4, 7)
         * + member 의 연관관계인 team 엔티티 조회 3 건
         *
         * 메서드 이름 조회를 하면..
         * 그에 따른 JPQL 이 만들어져서 조회가 된다.
         * Eager 전략이므로 추가 쿼리가 나간다. (한방 쿼리가 만들어지는것은 아닌것을 알 수 있다.)
         * -> 메서드 이름 조회를 하면 철저하게 메서드 이름 조회에 맞게 JPQL 쿼리가 만들어져서 수행되는 것이다.
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

    @DisplayName("Eager 전략인데 메서드 이름 조회를 하면... N + 1 문제가 나타나지만 Entity Graph 로 해결했다.")
    @Test
    void findEntityGraphByIdIn() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * 쿼리 1 건 나감 (N + 1 문제해결, Entity Graph)
         */
        System.out.println("===============when, 쿼리===============");
        List<MemberEager> result = memberEagerRepository.findEntityGraphByIdIn(List.of(1L, 2L, 3L));
        System.out.println("===============when, 쿼리===============");

        // then
        /**
         * 추가 쿼리 없음
         */
        result.forEach(
                member -> assertThat(persistenceUnitUtil.isLoaded(member.getTeamEager())).isTrue() // EntityGraph(or 페치 조인) 로 이미 초기화됨 (Eager 전략으로 인한 초기화 보다 먼저이다.)
        );

        System.out.println("===============then, 추가 쿼리 확인===============");

        result.forEach(
                member -> {
                    member.getTeamEager().getName(); // 프록시 초기화
                });

        System.out.println("===============then, 추가 쿼리 확인===============");
    }
}