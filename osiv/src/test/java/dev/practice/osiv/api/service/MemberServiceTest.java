package dev.practice.osiv.api.service;

import dev.practice.osiv.domain.Member;
import dev.practice.osiv.domain.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("")
    @Test
    void test() {
        /**
         * 스프링 OSIV 는 기본적으로 interceptor 에서 적용된다.
         *
         * application.yml 에서 스프링 OSIV 를 적용하였지만,
         * Test 코드에서는 interceptor 를 통하여 memberService 가 실행되지 않기 때문에
         * OSIV 가 적용되지 않는다.
         *
         * 따라서, @Transactional 의 범위와 영속성 컨텍스트 범위가 동일하다.
         *
         * 여기서는 memberService 의 두 메서드에 각각 Transaction 이 걸려있다.
         */

        // given
        Member member = Member.create("member 1");

        // when
        Long saveId = memberService.join(member); // 트랜잭션 시작, 종료 되어 member 는 준영속 상태

        // then
        Member findMember = memberService.findOne(saveId); // 트랜잭션 시작, 종료 되어 findMember 는 준영속 상태

        // member 와 findMember 는 서로 다른 영속성 컨텍스트에서 비롯된 준영속 상태의 엔티티로 동일하지 않다.
        assertThat(member).isNotEqualTo(findMember);
    }


    @DisplayName("")
    @Test
    @Transactional // 트랜잭션 시작, 종료
    void test2() {
        /**
         * 스프링 OSIV 는 기본적으로 interceptor 에서 적용된다.
         *
         * application.yml 에서 스프링 OSIV 를 적용하였지만,
         * Test 코드에서는 interceptor 를 통하여 memberService 가 실행되지 않기 때문에
         * OSIV 가 적용되지 않는다.
         *
         * 따라서, @Transactional 의 범위와 영속성 컨텍스트 범위가 동일하다.
         *
         * 여기서는 test code 전체에 transaction 이 걸려있어서
         * memberService 의 두 메서드에서 사용되는 영속성 컨텍스트가 동일하다.
         */

        // given
        Member member = Member.create("member 1");

        // when
        Long saveId = memberService.join(member); // 기존 트랜잭션에 참여, member 는 영속 상태

        // then
        Member findMember = memberService.findOne(saveId); // 기존 트랜잭션에 참여, findMember 는 영속 상태

        // member 와 findMember 는 동일한 영속성 컨텍스트에서 비롯된 영속 상태의 엔티티로 동일하다.
        assertThat(member).isEqualTo(findMember);
    }
}