package com.example.clubservice.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.example.clubservice.domain.Club;
import com.example.clubservice.domain.repo.ClubRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class ClubControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ClubRepo clubRepo;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void cleanUp() {
        clubRepo.deleteAll();
    }

    @DisplayName("GET, /clubs/IT 요청 시 200 OK와 IT 카테고리 동아리 목록이 반환된다")
    @Test
    public void getClubsByCategory_IT_Success() throws Exception {
        // given
        final String url = "/clubs/IT";

        // when
        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$").isArray()
                );
    }

    @DisplayName("DELETE, /clubs/{id} 요청 시 200 OK와 성공 메시지가 반환된다")
    @Test
    public void deleteClub_Success() throws Exception {
        // given
        Club club = clubRepo.findAll().get(0);
        Long clubId = club.getId();
        final String url = "/clubs/" + clubId;

        // when
        final ResultActions result = mockMvc.perform(delete(url));

        // then
        result
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content().string("성공적으로 삭제되었습니다.")
                );
    }
}