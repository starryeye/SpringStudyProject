package dev.practice.cascadeandorphan;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
        teamRepository.deleteAllInBatch();
    }

    @DisplayName("영속성 전이로 인해 team 을 영속화 시키면 member 도 함께 영속화 된다.")
    @Test
    void test() {

        // given
        Team team = Team.create("T1");

        Member member1 = Member.create("Alice");
        Member member2 = Member.create("Bob");
        Member member3 = Member.create("Claire");

        team.addMembers(List.of(member1, member2, member3));

        // when
        /**
         * cascade(영속성 전이) 에 의해 연관된 엔티티도 함께 영속 상태가 된다.
         * -> team 에 연관된 member 도 함께 영속화 된다.
         *
         * 영속 상태가 되려면 식별자가 필요하므로 member, team 모두 save 호출 즉시 적재됨
         * -> IDENTITY 전략, 그런데 @Transactional 이 걸려있지 않아서 save 메서드 내부 트랜잭션이다.
         */
        teamRepository.save(team);

        // then
        List<Member> result = memberRepository.findAll();

        assertThat(result).hasSize(3)
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "Alice", "Bob", "Claire"
                );
    }

    @DisplayName("영속성 전이는 collection add 메서드로 추가 하지 않으면 동작하지 않는다.")
    @Test
    void test2() {

        // given
        Team team = Team.create("T1");

        Member member1 = Member.create("Alice");
        Member member2 = Member.create("Bob");
        Member member3 = Member.create("Claire");

        member1.setTeam(team);
        member2.setTeam(team);
        member3.setTeam(team);

        // when
        teamRepository.save(team);

        // then
        List<Member> result = memberRepository.findAll();

        assertThat(result).isEmpty();
    }
}