package com.example.clubservice.service;

import com.example.clubservice.dto.request.ClubCreateRequest;
import com.example.clubservice.entity.Club;
import com.example.clubservice.enums.ClubCategory;
import com.example.clubservice.repository.ClubRepository;
import com.example.clubservice.exception.CustomException;
import com.example.clubservice.dto.response.ClubListResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClubServiceTest {

    @Autowired
    private ClubService clubService;

    @Autowired
    private ClubRepository clubRepo;

    @DisplayName("IT 카테고리로 동아리 목록을 조회하면 4개의 동아리가 반환된다")
    @Test
    void findAllByCategory_IT_Success() {
        // given
        ClubCategory category = ClubCategory.IT;

        // when
        List<ClubListResponse> result = clubService.findAllByCategory(category);

        // then
        assertThat(result)
                .isNotEmpty()
                .hasSize(4)
                .extracting(ClubListResponse::getName).contains("BIND");

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
        ClubCreateRequest request = new ClubCreateRequest("테스트 동아리", "테스트 설명", ClubCategory.SPORTS);

        // when
        clubService.createClub(request);
        Club result = clubRepo.findByName(request.getName());

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getName()).isEqualTo("테스트 동아리");
        Assertions.assertThat(result.getCategory()).isEqualTo(ClubCategory.SPORTS);
    }

    @Test
    @DisplayName("전체 동아리 조회 성공")
    void getAllClubs_Success() {
        // when
        List<ClubListResponse> result = clubService.getAllClubs();

        // then
        assertThat(result)
                .hasSize(8)
                .isNotEmpty();
    }

    @Test
    @DisplayName("동아리 생성 실패 - 중복된 이름")
    void createClub_DuplicateName_ThrowsException() {
        Club existingClub = Club.builder()
                .name("테스트 동아리")
                .description("기존 설명")
                .category(ClubCategory.SPORTS)
                .build();
        clubRepo.save(existingClub);

        ClubCreateRequest request = new ClubCreateRequest("테스트 동아리", "새로운 설명", ClubCategory.SPORTS);

        assertThrows(CustomException.class, () -> clubService.createClub(request));
    }

    @Test
    @DisplayName("전체 동아리 조회 실패 - 동아리가 없음")
    void getAllClubs_NoClubs_ReturnsEmptyList() {
        // given
        clubRepo.deleteAll();

        // when
        List<ClubListResponse> clubs = clubService.getAllClubs();

        // then
        assertTrue(clubs.isEmpty());
    }
}