package com.example.clubservice.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.clubservice.entity.Club;
import com.example.clubservice.repo.ClubRepo;
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
    public void getClubsByCategory_success() throws Exception {
        // given
        final String url = "/clubs/IT";

        // when
        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isArray());
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("성공적으로 삭제되었습니다."));
    }

    @DisplayName("GET, /clubs/INVALID 요청 시 400 Bad Request가 반환된다")
    @Test
    public void getClubsByCategory_InvalidCategory_Fail() throws Exception {
        // given
        final String url = "/clubs/INVALID_CATEGORY";

        // when
        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isBadRequest());
    }

    @DisplayName("DELETE, /clubs/{id} 요청 시 존재하지 않는 ID면 404 Not Found가 반환된다")
    @Test
    public void deleteClub_NotFound() throws Exception {
        // given
        Long nonExistentId = 99999L;
        final String url = "/clubs/" + nonExistentId;

        // when
        final ResultActions result = mockMvc.perform(delete(url));

        // then
        result
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());
    }
}