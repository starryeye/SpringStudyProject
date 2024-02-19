package dev.practice.nplusone.eager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamEagerRepositoryTest {

    /**
     * XXXToMany 에 대한 test
     *
     * XXXToOne 과 비교해본다.
     *
     */

    @Autowired
    private TeamEagerRepository teamEagerRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("XXXToMany 에서 Eager 전략을 사용하고 단일 엔티티 조회를 하면, 연관관계에 있는 컬렉션 엔티티까지 함께 조회하여 추가 쿼리가 없다.")
    @Test
    void findById() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        System.out.println("===============when, 쿼리===============");
        TeamEager result = teamEagerRepository.findById(1L).orElseThrow();
        System.out.println("===============when, 쿼리===============");

        // then
        assertThat(persistenceUnitUtil.isLoaded(result.getMemberEagers())).isTrue();
        /**
         * 추가 쿼리 없음 (N + 1 문제 해결, Eager 전략)
         */
        System.out.println("===============then, 추가 쿼리 확인===============");
        result.getMemberEagers().forEach(
                MemberEager::getName
        );
        System.out.println("===============then, 추가 쿼리 확인===============");
    }

    @DisplayName("XXXToMany 에서 Eager 전략을 사용하고 전체 엔티티 조회를 하면 N + 1 문제가 나타날 수 있다.")
    @Test
    void findAll() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * XXXToOne 관계와 다르게 XXXToMany 관계에서는 findAll 을 사용하면 N + 1 문제가 발생한다.
         *
         * N + 1 문제 발생..
         * 1 건의 쿼리(Team 전체 조회) 와 team 개수(N)만큼의 쿼리가 추가로 수행된다.
         */
        System.out.println("===============when, 쿼리===============");
        List<TeamEager> result = teamEagerRepository.findAll();
        System.out.println("===============when, 쿼리===============");

        // then
        /**
         * todo
         * Eager 전략이라서(??) when 단계에서 추가 쿼리를 수행하였으므로 then 에서는 추가 쿼리 없다.
         */
        System.out.println("===============then, 추가 쿼리 확인===============");
        result.forEach(
                teamEager -> {
                    assertThat(persistenceUnitUtil.isLoaded(teamEager.getMemberEagers())).isTrue();

                    teamEager.getMemberEagers().forEach(
                            MemberEager::getName
                    );
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
         * XXXToOne 관계와 동일하게 XXXToMany 관계에서도 메서드 이름 조회를 하면 N + 1 문제가 발생한다.
         *
         * N + 1 문제 발생..
         * 1 건의 쿼리(Team 조회) 와 team 개수(N)만큼의 쿼리가 추가로 수행된다.
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
                    assertThat(persistenceUnitUtil.isLoaded(teamEager.getMemberEagers())).isTrue();

                    teamEager.getMemberEagers().forEach(
                            MemberEager::getName
                    );
                }
        );
        System.out.println("===============then, 추가 쿼리 확인===============");
    }

    @DisplayName("Eager 전략인데 메서드 이름 조회를 하면... N + 1 문제가 나타나지만 Entity Graph 로 해결했다.")
    @Test
    void findEntityGraphByIdIn() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * 쿼리 1 건 발생
         * EntityGraph 를 활용하여 N + 1 문제를 해결함.
         */
        System.out.println("===============when, 쿼리===============");
        List<TeamEager> result = teamEagerRepository.findEntityGraphByIdIn(List.of(1L, 2L, 3L));
        System.out.println("===============when, 쿼리===============");

        // then
        /**
         * 추가 쿼리 없음
         */
        System.out.println("===============then, 추가 쿼리 확인===============");
        result.forEach(
                teamEager -> {
                    assertThat(persistenceUnitUtil.isLoaded(teamEager.getMemberEagers())).isTrue();

                    teamEager.getMemberEagers().forEach(
                            MemberEager::getName
                    );
                }
        );
        System.out.println("===============then, 추가 쿼리 확인===============");
    }
}