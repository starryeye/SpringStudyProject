package dev.practice.pagingandsorting.controller;

import dev.practice.pagingandsorting.repository.Address;
import dev.practice.pagingandsorting.repository.Member;
import dev.practice.pagingandsorting.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc // MockMvc 주입을 위함
@SpringBootTest // Controller 는 @WebMvcTest 로 mocking test 가 일반적이나.. 여기선 전체 테스트를 위해 integration test 로 한다.
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("Controller 에서 Pageable 을 사용할 수 있다. 첫 페이지 조회")
    @Test
    void firstPage() throws Exception {

        // given
        Member member1 = Member.create("member", LocalDate.of(2024, 1, 1), Address.create("city 1", "street 1", "zipcode 1"));
        Member member2 = Member.create("member", LocalDate.of(2024, 1, 2), Address.create("city 2", "street 2", "zipcode 2"));
        Member member3 = Member.create("member", LocalDate.of(2024, 1, 3), Address.create("city 3", "street 3", "zipcode 3"));
        Member member4 = Member.create("member diff", LocalDate.of(2024, 1, 4), Address.create("city 4", "street 4", "zipcode 4"));
        Member member5 = Member.create("member", LocalDate.of(2024, 1, 4), Address.create("city 1", "street 1", "zipcode 1"));
        Member member6 = Member.create("member", LocalDate.of(2024, 1, 5), Address.create("city 2", "street 2", "zipcode 2"));
        memberRepository.saveAll(List.of(member1, member2, member3, member4, member5, member6));

        // when
        // then
        mockMvc.perform(
                        get("/members")
                                .queryParam("name", "member")
                                .queryParam("page", "0")
                                .queryParam("size", "3")
                                .queryParam("sort", "DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray()) // 조회된 데이터
                .andExpect(jsonPath("$.pageable").exists()); // 조회한 페이지 정보

        /**
         * 참고, client 에서 반환 받은 page 데이터에서는 MemberRepositoryTest 에서 본 것 처럼
         * page.nextPageable() 등을 당연히 사용하지 못한다(json 에 불과함)
         *
         * $.pageable 에는 조회한 현재 페이지와 전체 페이지에 대한 정보가 있으며 해당 정보를 조합하여
         * 새로운 pageable 을 만들어 다음 페이지 조회에 사용하면 된다.
         */
    }

    @DisplayName("Controller 에서 Pageable 을 사용할 수 있다. 마지막 페이지 조회")
    @Test
    void lastPage() throws Exception {

        // given
        Member member1 = Member.create("member", LocalDate.of(2024, 1, 1), Address.create("city 1", "street 1", "zipcode 1"));
        Member member2 = Member.create("member", LocalDate.of(2024, 1, 2), Address.create("city 2", "street 2", "zipcode 2"));
        Member member3 = Member.create("member", LocalDate.of(2024, 1, 3), Address.create("city 3", "street 3", "zipcode 3"));
        Member member4 = Member.create("member diff", LocalDate.of(2024, 1, 4), Address.create("city 4", "street 4", "zipcode 4"));
        Member member5 = Member.create("member", LocalDate.of(2024, 1, 4), Address.create("city 1", "street 1", "zipcode 1"));
        Member member6 = Member.create("member", LocalDate.of(2024, 1, 5), Address.create("city 2", "street 2", "zipcode 2"));
        memberRepository.saveAll(List.of(member1, member2, member3, member4, member5, member6));

        // when
        // then
        mockMvc.perform(
                        get("/members")
                                .queryParam("name", "member")
                                .queryParam("page", "1")
                                .queryParam("size", "3")
                                .queryParam("sort", "DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray()) // 조회된 데이터
                .andExpect(jsonPath("$.pageable").exists()); // 조회한 페이지 정보

        /**
         * 참고로 마지막 페이지를 벗어나는 요청을 하면, content 가 비어있는 pageable 이 반환된다.
         */
    }
}