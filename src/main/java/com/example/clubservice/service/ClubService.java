package com.example.clubservice.service;

import com.example.clubservice.enums.ClubCategory;
import com.example.clubservice.dto.res.ClubListRes;

import java.util.List;

public interface ClubService {
    List<ClubListRes> findAllByCategory(ClubCategory category);
    void deleteClub(Long id);
}
