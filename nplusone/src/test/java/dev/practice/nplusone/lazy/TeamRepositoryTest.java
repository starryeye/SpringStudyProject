package dev.practice.nplusone.lazy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamRepositoryTest {

    //XXXToMany test

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("Lazy 전략에서는 엔티티를 조회 하면 연관관계인 컬렉션 엔티티는 조회하지 않고 실제 사용할때 조회된다.")
    @Test
    @Transactional
    void findAll() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        /**
         * 쿼리 1 건 수행
         */
        System.out.println("===============when, 쿼리===============");
        List<Team> result = teamRepository.findAll();
        System.out.println("===============when, 쿼리===============");

        // then
        /**
         * Lazy loading 으로 3 회 추가 쿼리 수행
         */
        System.out.println("===============then, 추가 쿼리 확인===============");
        result.forEach(
                team -> {
                    assertThat(persistenceUnitUtil.isLoaded(team.getMembers())).isFalse(); // Lazy 전략이라 초기화 안됨


                    /**
                     * 주의 사항!
                     *
                     * team 에서 member 는 XXXToMany 연관관계이다.
                     * for loop 로 member 를 하나씩 초기화 하여 총 9 회의 추가쿼리가 나갈 것 같지만..
                     * 컬렉션 엔티티를 초기화 할 땐 아직 접근하지 않은 엔티티도 함께 한번에 초기화한다.
                     * 그래서 총 3 회의 추가 쿼리가 나간다.
                     */
                    team.getMembers().forEach(
                            Member::getName
                    );
                }
        );

        // todo, 아래 이유 파악 필요..
//        for (Team team : result) {
//            List<Member> members = team.getMembers();
//            System.out.println("hello");
//            for(int i = 0; i < 3; i++) {
//                Member member = members.get(i); // <- 최초 여기서 프록시 초기화 함...
//                System.out.println("spring");
//                member.getName(); // <- 최초 여기서 프록시 초기화할 것으로 예상...
//            }
//        }

        System.out.println("===============then, 추가 쿼리 확인===============");
    }
}