package dev.practice.coupling.phase1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.practice.coupling.dto.RegisterMemberRequest;
import dev.practice.coupling.dto.RegisterMemberResponse;
import dev.practice.coupling.repository.EmailSendHistory;
import dev.practice.coupling.repository.EmailSendHistoryRepository;
import dev.practice.coupling.repository.Member;
import dev.practice.coupling.repository.MemberRepository;
import dev.practice.coupling.repository.enums.EmailType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EmailSendHistoryRepository emailSendHistoryRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
        emailSendHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("phase 1 통합 테스트")
    @Test
    void registerMember() throws Exception {

        // given
        String name = "name";
        String email = "email@email.com";
        RegisterMemberRequest request = new RegisterMemberRequest(name, email);

        // when
        // then
        MvcResult result = mockMvc.perform(
                        post("/api/v1/phase1/new")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // api 호출 결과 추출
        String jsonResponse = result.getResponse().getContentAsString();
        RegisterMemberResponse response = objectMapper.readValue(jsonResponse, RegisterMemberResponse.class);

        Long memberId = response.memberId();

        // member 저장 데이터 확인
        Member savedMember = memberRepository.findById(memberId).orElseThrow();

        assertThat(savedMember.getName()).isEqualTo(name);
        assertThat(savedMember.getEmail()).isEqualTo(email);

        // 감사 이메일 발송 데이터 확인
        List<EmailSendHistory> savedEmailHistory = emailSendHistoryRepository.findByTargetMemberId(memberId);

        assertThat(savedEmailHistory).hasSize(1);
        assertThat(savedEmailHistory.get(0).getMessage()).isEqualTo(name + ", Thank you for using our service! (phase 1)");
        assertThat(savedEmailHistory.get(0).getEmailType()).isEqualTo(EmailType.MEMBER_REGISTER_SUCCESS);
    }
}
