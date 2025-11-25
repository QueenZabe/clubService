package com.example.clubservice.service;

import com.example.clubservice.dto.req.ClubCreateReq;
import com.example.clubservice.dto.res.ClubRes;
import com.example.clubservice.enums.ClubCategory;
import com.example.clubservice.dto.res.ClubListRes;

import java.util.List;

public interface ClubService {
    ClubListRes createClub(ClubCreateReq request);
    List<ClubListRes> getAllClubs();

    List<ClubListRes> findAllByCategory(ClubCategory category);
    void deleteClub(Long id);
}
