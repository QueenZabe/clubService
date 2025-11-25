package com.example.clubservice.service;

import com.example.clubservice.domain.Club;
import com.example.clubservice.domain.enums.ClubCategory;
import com.example.clubservice.domain.repo.ClubRepo;
import com.example.clubservice.etc.exception.CustomException;
import com.example.clubservice.etc.exception.error.ErrorCode;
import com.example.clubservice.presentation.dto.res.ClubListRes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClubServiceImpl implements ClubService{

    private final ClubRepo clubRepo;

    @Override
    public List<ClubListRes> findAllByCategory(ClubCategory category) {
        if (category == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        List<Club> clubs = clubRepo.findAllByCategory(category);

        if (clubs.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        return clubs.stream()
                .map(ClubListRes::from)
                .toList();
    }

    @Override
    public void deleteClub(Long id) {
        if (id == null || id <= 0) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        if (!clubRepo.existsById(id)) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        clubRepo.deleteById(id);
    }
}
