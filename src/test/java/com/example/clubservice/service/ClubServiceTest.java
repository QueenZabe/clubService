package com.example.clubservice.service;

import com.example.clubservice.domain.Club;
import com.example.clubservice.domain.enums.ClubCategory;
import com.example.clubservice.domain.repo.ClubRepo;
import com.example.clubservice.presentation.dto.res.ClubListRes;
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
}