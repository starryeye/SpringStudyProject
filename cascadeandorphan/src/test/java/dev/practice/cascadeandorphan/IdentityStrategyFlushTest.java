package dev.practice.cascadeandorphan;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
public class IdentityStrategyFlushTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("IDENTITY 전략에서 save 를 호출하는 행위는 flush 가 아니고 insert 만 DB 에 전달됨")
    @Test
    @Transactional
    void test() {

        // given
        Member member1 = Member.create("member 1");

        memberRepository.save(member1);

        // 영속성 컨텍스트 초기화 및 flush
        entityManager.flush();
        entityManager.clear();


        // when
        Member member = memberRepository.findById(member1.getId()).orElseThrow();
        member.changeName("change name");

        Member member2 = Member.create("member 2");


        log.info("-----------------------before save---------------------------");
        /**
         * IDENTITY 전략에서 save 가 flush 이면 member1 의 이름 변경 쿼리도 함께 전달될 것이다.
         * flush 가 아니라면 after save 로그 이후 이름 변경 쿼리가 전달될 것이다.
         */
        memberRepository.save(member2);
        log.info("-----------------------after save---------------------------");

        // then
    }
}
