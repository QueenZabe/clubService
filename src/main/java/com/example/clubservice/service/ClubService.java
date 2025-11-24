package com.example.clubservice.service;

import com.example.clubservice.domain.enums.ClubCategory;
import com.example.clubservice.presentation.dto.res.ClubListRes;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClubService {
    List<ClubListRes> findAllByCategory(ClubCategory category);
    void deleteClub(Long id);
}
