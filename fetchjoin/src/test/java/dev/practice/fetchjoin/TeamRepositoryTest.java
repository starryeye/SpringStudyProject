package dev.practice.fetchjoin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("페치 조인을 사용하면, 팀을 조회 하면서 연관된 회원 컬렉션도 함께 조회할 수 있다.")
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
        List<Team> result = teamRepository.findAllByNameWithFetchJoin("team 1");

        // then
        /**
         * distinct 를 하지 않아 "동일"한 "team 1" 엔티티가 2건이 담겨 있어야 하지만...
         * hibernate 6 버전 부터는 페치 조인시.. 자동으로 distinct 적용되어 중복이 없어져서 1건만 조회된다.
         *
         * https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#hql-distinct
         */
        assertThat(result).hasSize(1)
                .allSatisfy(
                        team -> {
                            assertThat(team.getName()).isEqualTo("team 1");
                            team.getMembers().forEach(
                                    member -> {
                                        assertThat(persistenceUnitUtil.isLoaded(member)).isTrue();
                                    }
                            );

                            assertThat(team.getMembers()).hasSize(2)
                                    .extracting(Member::getName)
                                    .containsExactlyInAnyOrder("member 1", "member 2");
                        }
                );

    }

}