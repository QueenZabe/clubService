package com.example.clubservice.service;

import com.example.clubservice.dto.req.ClubUpdateReq;
import com.example.clubservice.dto.res.ClubRes;
import com.example.clubservice.enums.ClubCategory;
import com.example.clubservice.dto.res.ClubListRes;

import java.util.List;

public interface ClubService {
    List<ClubListRes> findAllByCategory(ClubCategory category);

    void deleteClub(Long id);

    ClubRes updateClub(Long id, ClubUpdateReq updateReq);
}
