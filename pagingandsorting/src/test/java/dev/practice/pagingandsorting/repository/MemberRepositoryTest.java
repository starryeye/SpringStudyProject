package dev.practice.pagingandsorting.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("쿼리 메서드에서 Sort 를 사용하여 정렬 기능을 사용할 수 있다.")
    @Test
    void sort() {

        // given
        Member member1 = Member.create("member", LocalDate.of(2024, 1, 1), Address.create("city 1", "street 1", "zipcode 1"));
        Member member2 = Member.create("member", LocalDate.of(2024, 1, 2), Address.create("city 2", "street 2", "zipcode 2"));
        Member member3 = Member.create("member", LocalDate.of(2024, 1, 3), Address.create("city 3", "street 3", "zipcode 3"));
        Member member4 = Member.create("member diff", LocalDate.of(2024, 1, 4), Address.create("city 4", "street 4", "zipcode 4"));
        memberRepository.saveAll(List.of(member1, member2, member3, member4));

        Sort sort = Sort.by(Sort.Direction.DESC, "registrationDate");

        // when
        log.info("------------------------before when-----------------------------");
        List<Member> result = memberRepository.findByName("member", sort);
        log.info("------------------------after when-----------------------------");

        // then
        assertThat(result).hasSize(3)
                .extracting("name", "registrationDate")
                .containsExactly(
                        Tuple.tuple("member", LocalDate.of(2024, 1, 3)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 2)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 1))
                );
    }
}