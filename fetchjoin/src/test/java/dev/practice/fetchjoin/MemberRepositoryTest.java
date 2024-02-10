package dev.practice.fetchjoin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

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

    @DisplayName("페치 조인을 사용해서 회원 엔티티를 조회하면서 연관된 팀 엔티티도 함께 조회한다.")
    @Test
    void test() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        /**
         * member 1 -> team 1
         * member 2 -> team 1
         * member 3 -> team 2
         */
        Member member1 = Member.create("member 1");
        Member member2 = Member.create("member 2");
        Member member3 = Member.create("member 3");
        Member member4 = Member.create("member 4");
        Team team1 = Team.create("team 1");
        Team team2 = Team.create("team 2");
        Team team3 = Team.create("team 3");

        team1.addMembers(List.of(member1, member2));
        team2.addMembers(List.of(member3));

        teamRepository.saveAll(List.of(team1, team2, team3));
        memberRepository.save(member4);

        // when
        /**
         * 엔티티 페치 조인 (ManyToOne)
         *
         * 글로벌 페치 전략이 Lazy 임에도 불구하고
         * fetch join 을 사용하면 member 에 연관된 team 까지 한방에 조회한다.
         */
        log.info("------------------- before when ----------------------");
        List<Member> result = memberRepository.findAllBy();
        log.info("------------------- after when ----------------------");

        // then
        Map<String, String> expectedMemberToTeamMap = Map.of(
                "member 1", "team 1",
                "member 2", "team 1",
                "member 3", "team 2"
        );

        assertThat(result).hasSize(3)
                .allSatisfy(
                        member -> {
                            assertThat(persistenceUnitUtil.isLoaded(member.getTeam()))
                                    .isTrue();
                            assertThat(member.getName())
                                    .isIn(expectedMemberToTeamMap.keySet());
                            assertThat(member.getTeam().getName())
                                    .isEqualTo(expectedMemberToTeamMap.get(member.getName()));
                        }
                );
    }
}