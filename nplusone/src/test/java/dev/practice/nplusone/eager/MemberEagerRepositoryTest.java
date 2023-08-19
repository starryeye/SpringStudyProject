package dev.practice.nplusone.eager;

import dev.practice.nplusone.lazy.Team;
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

    @DisplayName("Eager 전략에서는 ManyToOne 관계에서 findAll 을 하여 조회 하면 조회 된 만큼 추가 쿼리가 안나간다.")
    @Test
    void findAll_And_N_Plus_One_Problem() {

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
                member -> assertThat(persistenceUnitUtil.isLoaded(member.getTeamEager())).isTrue()
        );

        /**
         * 3 건의 쿼리가 나감
         */
        System.out.println("===============then, Lazy Loading 으로 인한 추가 쿼리 확인===============");

        Set<TeamEager> teams = new HashSet<>();

        result.forEach(
                member -> {
                    member.getTeamEager().getName(); // 프록시 초기화
                    teams.add(member.getTeamEager());
                });

        System.out.println("===============then, Lazy Loading 으로 인한 추가 쿼리 확인===============");
    }

    @DisplayName("Eager 전략에서 JPQL 을 사용하여 조회 시 N + 1 추가 쿼리가 발생할 수 있다.")
    @Test
    void findMemberEagerBy() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * 2 건의 쿼리가 나간다!!
         *
         * "select m from MemberEager m where m.id = :id"
         * 위 JPQL 을 우선적으로 수행하고.. 이후 Eager 전략이므로 추가 쿼리가 수행된다.
         *
         * 결론
         * Eager 전략을 Entity 에 선언 하였더라도 JPQL 을 사용할 경우, 의도 대로 한방 쿼리를 수행하기 위해서는..
         * JPQL 에서 잊지 않고 fetch join 을 해줘야한다.
         */
        System.out.println("===============when, 쿼리===============");
        MemberEager member = memberEagerRepository.findMemberEagerBy(1L).orElseThrow();
        System.out.println("===============when, 쿼리===============");

        // then
        /**
         * 여기서는 위에서 이미 조회가 다 이루어 져서 프록시 초기화가 되어있는 결과가 나옴
         */
        assertThat(persistenceUnitUtil.isLoaded(member.getTeamEager())).isTrue();
    }
}