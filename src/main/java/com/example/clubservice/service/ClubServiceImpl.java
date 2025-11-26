package com.example.clubservice.service;

import com.example.clubservice.dto.request.ClubCreateRequest;
import com.example.clubservice.dto.request.ClubUpdateRequest;
import com.example.clubservice.entity.Club;
import com.example.clubservice.enums.ClubCategory;
import com.example.clubservice.repository.ClubRepository;
import com.example.clubservice.exception.CustomException;
import com.example.clubservice.exception.error.ErrorCode;
import com.example.clubservice.dto.response.ClubListResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClubServiceImpl implements ClubService{

    private final ClubRepository clubRepository;

    @Override
    public void createClub(ClubCreateRequest request) {
        if (clubRepository.existsByName(request.getName())) {
            throw new CustomException(ErrorCode.ALREADY_EXISTS_CLUB);
        }

        Club club = Club.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .createdAt(LocalDateTime.now())
                .build();

        clubRepository.save(club);
    }

    @Override
    public List<ClubListResponse> getAllClubs() {
        return clubRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ClubListResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClubListResponse> findAllByCategory(ClubCategory category) {
        if (category == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        List<Club> clubs = clubRepository.findAllByCategory(category);

        if (clubs.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        return clubs.stream()
                .map(ClubListResponse::from)
                .toList();
    }

    @Override
    public void updateClub(Long id, ClubUpdateRequest updateRequest) {
        if (id == null || id <= 0 || updateRequest == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        club.update(updateRequest);

        clubRepository.save(club);
    }

    @Override
    public void deleteClub(Long id) {
        if (id == null || id <= 0) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        if (!clubRepository.existsById(id)) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        clubRepository.deleteById(id);
    }
}