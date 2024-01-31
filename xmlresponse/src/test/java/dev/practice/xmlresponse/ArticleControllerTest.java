package dev.practice.xmlresponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = {
                ArticleController.class
        }
)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("accept, application/json 으로 요청 시 json 으로 응답한다.")
    @Test
    void accept_application_json() throws Exception {

        // given
        // when
        // then
        mockMvc.perform(
                get("/articles")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.article.title").value("title"))
                .andExpect(jsonPath("$.article.content").value("content"))
                .andExpect(jsonPath("$.article.createdAt").value("2024-01-31 21:11:00"))
                .andExpect(jsonPath("$.article.nullProperty").doesNotExist());
    }

    @DisplayName("accept 없이 요청 시 xml 로 응답한다.")
    @Test
    void accept_all() throws Exception {

        // given
        // when
        // then
        mockMvc.perform(
                get("/articles")
        )
                .andDo(print())
                .andExpect(content().contentType("application/xml;charset=UTF-8"))
                .andExpect(status().isAccepted())
                .andExpect(xpath("/article/title").string("title"))
                .andExpect(xpath("/article/content").string("content"))
                .andExpect(xpath("/article/createdAt").string("2024-01-31T21:11:00"))
                .andExpect(content().xml("<article><title>title</title><content>content</content><createdAt>2024-01-31T21:11:00</createdAt><nullProperty/></article>"));

    }
}