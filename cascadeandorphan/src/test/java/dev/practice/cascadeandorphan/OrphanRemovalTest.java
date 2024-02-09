package dev.practice.cascadeandorphan;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class OrphanRemovalTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
        teamRepository.deleteAllInBatch();
    }

    @DisplayName("고아 객체 제거, team 이 가지고 있는 member collection 에서 특정 member 를 삭제하면 실제로 삭제가 된다.")
    @Test
    @Transactional
    void orphan1() {

        /**
         * 원래 orphanRemoval 옵션의 기본값인 false 로 놔두고..
         * team 의 member collection 에서 특정 member 를 삭제하면 db 에는 아무런 조치가 없다.
         * 왜냐하면, 연관관계 주인은 member 이기 때문이다.
         */

        // given
        Team givenTeam = Team.create("T1");

        Member member1 = Member.create("Alice");
        Member member2 = Member.create("Bob");
        Member member3 = Member.create("Claire");

        givenTeam.addMembers(List.of(member1, member2, member3));

        teamRepository.save(givenTeam); // team, member 모두 저장

        // when
        Team team = teamRepository.findTeamById(givenTeam.getId()).orElseThrow();

        log.info("-----------------before, member remove from team collection-----------------");
        team.removeMemberByMemberName("Alice");

        entityManager.flush();
        entityManager.clear();
        log.info("-----------------after, member remove from team collection-----------------");


        // then
        Optional<Member> result = memberRepository.findByName("Alice");

        assertThat(result).isEmpty();
    }
}
