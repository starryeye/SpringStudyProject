package dev.practice.pagingandsorting.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @DisplayName("쿼리 메서드에서 Pageable 을 사용하여 페이징 기능을 사용할 수 있다.")
    @Test
    void pageable() {

        // given
        Member member1 = Member.create("member", LocalDate.of(2024, 1, 1), Address.create("city 1", "street 1", "zipcode 1"));
        Member member2 = Member.create("member", LocalDate.of(2024, 1, 2), Address.create("city 2", "street 2", "zipcode 2"));
        Member member3 = Member.create("member", LocalDate.of(2024, 1, 3), Address.create("city 3", "street 3", "zipcode 3"));
        Member member4 = Member.create("member diff", LocalDate.of(2024, 1, 4), Address.create("city 4", "street 4", "zipcode 4"));
        Member member5 = Member.create("member", LocalDate.of(2024, 1, 4), Address.create("city 1", "street 1", "zipcode 1"));
        Member member6 = Member.create("member", LocalDate.of(2024, 1, 5), Address.create("city 2", "street 2", "zipcode 2"));
        Member member7 = Member.create("member", LocalDate.of(2024, 1, 6), Address.create("city 3", "street 3", "zipcode 3"));
        Member member8 = Member.create("member diff", LocalDate.of(2024, 1, 7), Address.create("city 4", "street 4", "zipcode 4"));
        Member member9 = Member.create("member", LocalDate.of(2024, 1, 7), Address.create("city 1", "street 1", "zipcode 1"));
        Member member10 = Member.create("member", LocalDate.of(2024, 1, 8), Address.create("city 2", "street 2", "zipcode 2"));
        Member member11 = Member.create("member", LocalDate.of(2024, 1, 9), Address.create("city 3", "street 3", "zipcode 3"));
        Member member12 = Member.create("member diff", LocalDate.of(2024, 1, 9), Address.create("city 4", "street 4", "zipcode 4"));
        memberRepository.saveAll(List.of(member1, member2, member3, member4, member5, member6, member7, member8, member9, member10, member11, member12));

        /**
         * Pageable 인터페이스의 구현체는 PageRequest 이다.
         */
        Sort sort = Sort.by(Sort.Direction.DESC, "registrationDate");
        PageRequest pageRequest1 = PageRequest.of(0, 3, sort);
        PageRequest pageRequest2 = PageRequest.of(1, 3, sort);
        PageRequest pageRequest3 = PageRequest.of(2, 3, sort);

        // when
        /**
         * 리턴타입이 List 이므로 전체 count 쿼리는 DB 로 전송되지 않았다.
         */
        log.info("------------------------before when-----------------------------");
        List<Member> result1 = memberRepository.findListByName("member", pageRequest1);
        List<Member> result2 = memberRepository.findListByName("member", pageRequest2);
        List<Member> result3 = memberRepository.findListByName("member", pageRequest3);
        log.info("------------------------after when-----------------------------");

        // then
        assertThat(result1).hasSize(3)
                .extracting("name", "registrationDate")
                .containsExactly(
                        Tuple.tuple("member", LocalDate.of(2024, 1, 9)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 8)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 7))
                );
        assertThat(result2).hasSize(3)
                .extracting("name", "registrationDate")
                .containsExactly(
                        Tuple.tuple("member", LocalDate.of(2024, 1, 6)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 5)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 4))
                );
        assertThat(result3).hasSize(3)
                .extracting("name", "registrationDate")
                .containsExactly(
                        Tuple.tuple("member", LocalDate.of(2024, 1, 3)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 2)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 1))
                );
    }

    @DisplayName("쿼리 메서드에서 Pageable, Page 를 사용하여 페이징 기능을 사용할 수 있다.")
    @Test
    void page() {

        // given
        Member member1 = Member.create("member", LocalDate.of(2024, 1, 1), Address.create("city 1", "street 1", "zipcode 1"));
        Member member2 = Member.create("member", LocalDate.of(2024, 1, 2), Address.create("city 2", "street 2", "zipcode 2"));
        Member member3 = Member.create("member", LocalDate.of(2024, 1, 3), Address.create("city 3", "street 3", "zipcode 3"));
        Member member4 = Member.create("member diff", LocalDate.of(2024, 1, 4), Address.create("city 4", "street 4", "zipcode 4"));
        Member member5 = Member.create("member", LocalDate.of(2024, 1, 4), Address.create("city 1", "street 1", "zipcode 1"));
        Member member6 = Member.create("member", LocalDate.of(2024, 1, 5), Address.create("city 2", "street 2", "zipcode 2"));
        Member member7 = Member.create("member", LocalDate.of(2024, 1, 6), Address.create("city 3", "street 3", "zipcode 3"));
        Member member8 = Member.create("member diff", LocalDate.of(2024, 1, 7), Address.create("city 4", "street 4", "zipcode 4"));
        Member member9 = Member.create("member", LocalDate.of(2024, 1, 7), Address.create("city 1", "street 1", "zipcode 1"));
        Member member10 = Member.create("member", LocalDate.of(2024, 1, 8), Address.create("city 2", "street 2", "zipcode 2"));
        Member member11 = Member.create("member", LocalDate.of(2024, 1, 9), Address.create("city 3", "street 3", "zipcode 3"));
        Member member12 = Member.create("member diff", LocalDate.of(2024, 1, 9), Address.create("city 4", "street 4", "zipcode 4"));
        Member member13 = Member.create("member", LocalDate.of(2024, 1, 10), Address.create("city 1", "street 1", "zipcode 1"));
        Member member14 = Member.create("member", LocalDate.of(2024, 1, 11), Address.create("city 2", "street 2", "zipcode 2"));
        Member member15 = Member.create("member", LocalDate.of(2024, 1, 12), Address.create("city 3", "street 3", "zipcode 3"));
        Member member16 = Member.create("member diff", LocalDate.of(2024, 1, 12), Address.create("city 4", "street 4", "zipcode 4"));
        memberRepository.saveAll(List.of(member1, member2, member3, member4, member5, member6, member7, member8, member9, member10, member11, member12, member13, member14, member15, member16));

        /**
         * Pageable 인터페이스의 구현체는 PageRequest 이다.
         */
        Sort sort = Sort.by(Sort.Direction.DESC, "registrationDate");
        PageRequest pageRequest1 = PageRequest.of(0, 3, sort);
        PageRequest pageRequest2 = PageRequest.of(1, 3, sort);
        PageRequest pageRequest3 = PageRequest.of(2, 3, sort);
        PageRequest pageRequest4 = PageRequest.of(3, 3, sort);

        // when
        /**
         * 리턴타입이 Page 이므로 전체 count 쿼리가 DB 로 전송된다. (각각 1회씩, 총 4회)
         */
        log.info("------------------------before when-----------------------------");
        Page<Member> result1 = memberRepository.findPageByName("member", pageRequest1);
        Page<Member> result2 = memberRepository.findPageByName("member", pageRequest2);
        Page<Member> result3 = memberRepository.findPageByName("member", pageRequest3);
        Page<Member> result4 = memberRepository.findPageByName("member", pageRequest4);
        log.info("------------------------after when-----------------------------");

        // then
        // 전체 페이지 수
        assertThat(result1.getTotalPages()).isEqualTo(4);
        assertThat(result2.getTotalPages()).isEqualTo(4);
        assertThat(result3.getTotalPages()).isEqualTo(4);
        assertThat(result4.getTotalPages()).isEqualTo(4);

        // 전체 데이터 수
        assertThat(result1.getTotalElements()).isEqualTo(12);
        assertThat(result2.getTotalElements()).isEqualTo(12);
        assertThat(result3.getTotalElements()).isEqualTo(12);
        assertThat(result4.getTotalElements()).isEqualTo(12);

        // 현재 페이지 번호
        assertThat(result1.getNumber()).isEqualTo(0);
        assertThat(result2.getNumber()).isEqualTo(1);
        assertThat(result3.getNumber()).isEqualTo(2);
        assertThat(result4.getNumber()).isEqualTo(3);

        // 페이지 크기 (가질 수 있는 최대 항목 수)
        assertThat(result1.getSize()).isEqualTo(3);
        assertThat(result2.getSize()).isEqualTo(3);
        assertThat(result3.getSize()).isEqualTo(3);
        assertThat(result4.getSize()).isEqualTo(3);

        // 현재 페이지의 데이터 수 (마지막 페이지의 경우 페이지 크기와 다를 수 있다.)
        assertThat(result1.getNumberOfElements()).isEqualTo(3);
        assertThat(result2.getNumberOfElements()).isEqualTo(3);
        assertThat(result3.getNumberOfElements()).isEqualTo(3);
        assertThat(result4.getNumberOfElements()).isEqualTo(3);

        // 현재 페이지가 첫 페이지인지 여부
        assertThat(result1.isFirst()).isTrue();
        assertThat(result2.isFirst()).isFalse();
        assertThat(result3.isFirst()).isFalse();
        assertThat(result4.isFirst()).isFalse();

        // 현재 페이지가 마지막 페이지인지 여부
        assertThat(result1.isLast()).isFalse();
        assertThat(result2.isLast()).isFalse();
        assertThat(result3.isLast()).isFalse();
        assertThat(result4.isLast()).isTrue();

        // 다음 페이지 존재 여부
        assertThat(result1.hasNext()).isTrue();
        assertThat(result2.hasNext()).isTrue();
        assertThat(result3.hasNext()).isTrue();
        assertThat(result4.hasNext()).isFalse();

        // 이전 페이지 존재 여부
        assertThat(result1.hasPrevious()).isFalse();
        assertThat(result2.hasPrevious()).isTrue();
        assertThat(result3.hasPrevious()).isTrue();
        assertThat(result4.hasPrevious()).isTrue();

        // 다음 페이지의 pageable
        assertThat(result1.nextPageable()) // 다음 페이지 정보, 해당 pageable 로 다음 페이지 쿼리에 사용하면 편하다.
                .satisfies(
                        nextPageable -> {
                            assertThat(nextPageable.isPaged()).isTrue();
                            assertThat(nextPageable.getPageNumber()).isEqualTo(1); // (다음) 페이지 번호
                            assertThat(nextPageable.getPageSize()).isEqualTo(3); // (다음) 페이지의 최대 항목 수
                            assertThat(nextPageable.getOffset()).isEqualTo(3); // (다음) 페이지의 offset 시작 번호 (현재 페이지의 offset 이 0, 1, 2 이다.)
                            assertThat(nextPageable.getSort()).isEqualTo(sort); // (다음) 페이지의 정렬 정보
                        }
                );
        assertThat(result2.nextPageable()) // 다음 페이지 정보, 해당 pageable 로 다음 페이지 쿼리에 사용하면 편하다.
                .satisfies(
                        nextPageable -> {
                            assertThat(nextPageable.isPaged()).isTrue();
                            assertThat(nextPageable.getPageNumber()).isEqualTo(2); // (다음) 페이지 번호
                            assertThat(nextPageable.getPageSize()).isEqualTo(3); // (다음) 페이지의 최대 항목 수
                            assertThat(nextPageable.getOffset()).isEqualTo(6); // (다음) 페이지의 offset 시작 번호 (현재 페이지의 offset 이 3, 4, 5 이다.)
                            assertThat(nextPageable.getSort()).isEqualTo(sort); // (다음) 페이지의 정렬 정보
                        }
                );
        assertThat(result3.nextPageable()) // 다음 페이지 정보, 해당 pageable 로 다음 페이지 쿼리에 사용하면 편하다.
                .satisfies(
                        nextPageable -> {
                            assertThat(nextPageable.isPaged()).isTrue();
                            assertThat(nextPageable.getPageNumber()).isEqualTo(3); // (다음) 페이지 번호
                            assertThat(nextPageable.getPageSize()).isEqualTo(3); // (다음) 페이지의 최대 항목 수
                            assertThat(nextPageable.getOffset()).isEqualTo(9); // (다음) 페이지의 offset 시작 번호 (현재 페이지의 offset 이 6, 7, 8 이다.)
                            assertThat(nextPageable.getSort()).isEqualTo(sort); // (다음) 페이지의 정렬 정보
                        }
                );
        assertThat(result4.nextPageable()) // 다음 페이지 정보, 해당 pageable 로 다음 페이지 쿼리에 사용하면 편하다.
                .satisfies(
                        nextPageable -> {
                            assertThat(nextPageable.isPaged()).isFalse(); // 해당 페이지가 마지막 페이지라서 다음 페이지는 없다.
                        }
                );

        // 조회된 데이터
        assertThat(result1.getContent()).hasSize(3)
                .extracting("name", "registrationDate")
                .containsExactly(
                        Tuple.tuple("member", LocalDate.of(2024, 1, 12)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 11)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 10))
                );
        assertThat(result2.getContent()).hasSize(3)
                .extracting("name", "registrationDate")
                .containsExactly(
                        Tuple.tuple("member", LocalDate.of(2024, 1, 9)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 8)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 7))
                );
        assertThat(result3.getContent()).hasSize(3)
                .extracting("name", "registrationDate")
                .containsExactly(
                        Tuple.tuple("member", LocalDate.of(2024, 1, 6)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 5)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 4))
                );
        assertThat(result4.getContent()).hasSize(3)
                .extracting("name", "registrationDate")
                .containsExactly(
                        Tuple.tuple("member", LocalDate.of(2024, 1, 3)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 2)),
                        Tuple.tuple("member", LocalDate.of(2024, 1, 1))
                );
    }
}