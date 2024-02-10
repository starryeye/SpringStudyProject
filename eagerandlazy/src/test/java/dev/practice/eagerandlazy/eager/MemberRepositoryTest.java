package dev.practice.eagerandlazy.eager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
        teamRepository.deleteAllInBatch();
    }

    @DisplayName("ManyToOne 에서 Eager, 즉시 로딩 전략을 사용하면 한방쿼리이다.")
    @Test
    void test() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        Team team = Team.create("T1");

        teamRepository.save(team);

        Member member1 = Member.create("Alice", team);
        Member member2 = Member.create("Bob", team);
        Member member3 = Member.create("Claire", team);

        memberRepository.saveAll(List.of(member1, member2, member3));

        // when
        log.info("------------------------before when---------------------------");
        Member result = memberRepository.findById(member1.getId()).orElseThrow(); // Eager 를 사용하면 조인으로 한방 쿼리가 된다...
        log.info("------------------------after when---------------------------");

        // then
        assertThat(persistenceUnitUtil.isLoaded(result.getTeam())).isTrue();

    }

    @DisplayName("글로벌 페치 전략이 즉시 로딩이어도, JPQL 을 fetch join 을 사용하지 않고 그냥 join 을 사용하면 한방 쿼리가 아니다.")
    @Test
    void test2() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        Team team = Team.create("T1");

        teamRepository.save(team);

        Member member1 = Member.create("Alice", team);
        Member member2 = Member.create("Bob", team);
        Member member3 = Member.create("Claire", team);

        memberRepository.saveAll(List.of(member1, member2, member3));

        // when
        /**
         * 한방 쿼리는 아니지만, 글로벌 페치 전략이 즉시 로딩이므로..
         * 쿼리가 한번더 나간다.
         *
         * 즉, 글로벌 페치 전략보다 우선하는게 JPQL 이다.
         */
        log.info("------------------------before when---------------------------");
        Member result = memberRepository.findMemberByIdWithInnerJoin(member1.getId()).orElseThrow();
        log.info("------------------------after when---------------------------");

        // then
        assertThat(persistenceUnitUtil.isLoaded(result.getTeam())).isTrue();
    }
}