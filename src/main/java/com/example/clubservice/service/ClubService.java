package com.example.clubservice.service;

import com.example.clubservice.dto.request.ClubCreateRequest;
import com.example.clubservice.dto.request.ClubUpdateRequest;
import com.example.clubservice.dto.response.ClubResponse;
import com.example.clubservice.enums.ClubCategory;
import com.example.clubservice.dto.response.ClubListResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface ClubService {

    void createClub(ClubCreateRequest request);
    List<ClubListResponse> getAllClubs();
    ClubResponse getClubById(Long id);
    List<ClubListResponse> findAllByCategory(ClubCategory category);
    void updateClub(Long id, ClubUpdateRequest updateReq);
    void deleteClub(Long id);
    void writeClubExcel(OutputStream os) throws IOException;
}
