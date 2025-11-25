package com.example.clubservice.service;

import com.example.clubservice.dto.req.ClubUpdateReq;
import com.example.clubservice.dto.res.ClubRes;
import com.example.clubservice.entity.Club;
import com.example.clubservice.enums.ClubCategory;
import com.example.clubservice.repo.ClubRepo;
import com.example.clubservice.exception.CustomException;
import com.example.clubservice.exception.error.ErrorCode;
import com.example.clubservice.dto.res.ClubListRes;
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

    @Override
    public ClubRes updateClub(Long id, ClubUpdateReq updateReq) {
        if (id == null || id <= 0 || updateReq == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        Club club = clubRepo.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        club.update(updateReq);

        clubRepo.save(club);

        return ClubRes.from(club); // DTO로 반환
    }
}
