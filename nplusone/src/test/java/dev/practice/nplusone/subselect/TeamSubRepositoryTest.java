package dev.practice.nplusone.subselect;

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
class TeamSubRepositoryTest {

    @Autowired
    private TeamSubRepository teamSubRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("OneToMany 관계에서는 글로벌 페치 전략에 상관 없이 subselect 로 N + 1 문제를 해결할 수 있다.")
    @Test
    void findByIdGreaterThan() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * Eager 전략인 OneToMany 에서 JPQL(메서드 이름 조회 포함) 을 통해 엔티티를 조회하면
         * 엔티티 컬렉션은 Eager 전략에 의해 N + 1 문제가 발생한다. (배치 옵션 처럼 해결됨)
         *
         * 이를 Hibernate 에서 제공하는 @Fetch(value = FetchMode.SUBSELECT) 를 통해 해결해본다.
         *
         * 참고로 Lazy 일 경우엔 지연로딩 때 N + 1 문제가 해결된다. (배치 옵션 처럼 해결됨)
         *
         * when 에서 쿼리는 2 건 나간다. (team 들 조회, team 들에 대한 member 들 한방쿼리)
         * subselect 를 적용하지 않으면, 3 건 나간다. (team 들 조회, team 들에 대한 member 들을 팀 개수만큼 쿼리)
         */
        System.out.println("===============when, 쿼리===============");
        List<TeamSub> result = teamSubRepository.findByIdGreaterThan(1L);
        System.out.println("===============when, 쿼리===============");

        // then
        System.out.println("===============then, 추가 쿼리 확인===============");
        result.forEach(
                teamSub -> {
                    teamSub.getMemberSubs().forEach(
                            memberSub -> {
                                assertThat(persistenceUnitUtil.isLoaded(memberSub)).isTrue(); // Eager 전략으로 인해 이미 초기화
                                memberSub.getName(); // 프록시 초기화
                            }
                    );
                }
        );
        System.out.println("===============then, 추가 쿼리 확인===============");
    }

}