package com.example.clubservice.service;

import com.example.clubservice.dto.req.ClubCreateReq;
import com.example.clubservice.entity.Club;
import com.example.clubservice.enums.ClubCategory;
import com.example.clubservice.repo.ClubRepo;
import com.example.clubservice.exception.CustomException;
import com.example.clubservice.dto.res.ClubListRes;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.given;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ClubServiceTest {

    @Autowired
    private ClubService clubService;

    @Autowired
    private ClubRepo clubRepo;

    @DisplayName("IT 카테고리로 동아리 목록을 조회하면 4개의 동아리가 반환된다")
    @Test
    void findAllByCategory_IT_Success() {
        // given
        ClubCategory category = ClubCategory.IT;

        // when
        List<ClubListRes> result = clubService.findAllByCategory(category);

        // then
        assertThat(result)
                .isNotEmpty()
                .hasSize(4)
                .extracting(ClubListRes::getName).contains("BIND");

    }

    @DisplayName("존재하는 동아리 ID로 삭제하면 정상적으로 삭제된다")
    @Test
    void deleteClub_Success() {
        // given
        Club club = clubRepo.findAll().get(0);
        Long clubId = club.getId();

        // when
        clubService.deleteClub(clubId);

        // then
        assertThat(clubRepo.existsById(clubId)).isFalse();
    }

    @DisplayName("null 카테고리로 조회하면 BAD_REQUEST 예외가 발생한다")
    @Test
    void findAllByCategory_NullCategory_ThrowsException() {
        // given
        ClubCategory category = null;

        // when & then
        assertThrows(CustomException.class, () -> {
            clubService.findAllByCategory(category);
        });
    }

    @DisplayName("해당 카테고리에 동아리가 없으면 NOT_FOUND 예외가 발생한다")
    @Test
    void findAllByCategory_NoClubs_ThrowsException() {
        // given
        clubRepo.deleteAll(); // 모든 동아리 삭제
        ClubCategory category = ClubCategory.IT;

        // when & then
        assertThrows(CustomException.class, () -> {
            clubService.findAllByCategory(category);
        });
    }

    @DisplayName("null ID로 삭제하면 BAD_REQUEST 예외가 발생한다")
    @Test
    void deleteClub_NullId_ThrowsException() {
        // given
        Long clubId = null;

        // when & then
        assertThrows(CustomException.class, () -> {
            clubService.deleteClub(clubId);
        });
    }

    @DisplayName("0 이하의 ID로 삭제하면 BAD_REQUEST 예외가 발생한다")
    @Test
    void deleteClub_InvalidId_ThrowsException() {
        // given
        Long clubId = -1L;

        // when & then
        assertThrows(CustomException.class, () -> {
            clubService.deleteClub(clubId);
        });
    }

    @DisplayName("존재하지 않는 ID로 삭제하면 NOT_FOUND 예외가 발생한다")
    @Test
    void deleteClub_NotExist_ThrowsException() {
        // given
        Long nonExistentId = 99999L;

        // when & then
        assertThrows(CustomException.class, () -> {
            clubService.deleteClub(nonExistentId);
        });
    }

    @Test
    @DisplayName("동아리 생성 성공")
    void createClub_Success() {
        // given
        ClubCreateReq request = new ClubCreateReq("테스트 동아리", "테스트 설명", ClubCategory.SPORTS);

        // when
        ClubListRes result = clubService.createClub(request);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getName()).isEqualTo("테스트 동아리");
        Assertions.assertThat(result.getCategory()).isEqualTo("SPORTS");
    }

    @Test
    @DisplayName("전체 동아리 조회 성공")
    void getAllClubs_Success() {
        // given
        Club club1 = Club.builder()
                .name("동아리1")
                .description("설명1")
                .category(ClubCategory.SPORTS)
                .build();

        Club club2 = Club.builder()
                .name("동아리2")
                .description("설명2")
                .category(ClubCategory.IT)
                .build();

        Mockito.when(clubRepo.findAllOrderByCreatedAtDesc()).thenReturn(List.of(club1, club2));

        // when
        List<ClubListRes> result = clubService.getAllClubs();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("동아리1");
        assertThat(result.get(1).getName()).isEqualTo("동아리2");
        Mockito.verify(clubRepo).findAllOrderByCreatedAtDesc();
    }

}