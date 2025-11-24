package com.example.clubservice.service;

import com.example.clubservice.domain.Club;
import com.example.clubservice.domain.enums.ClubCategory;
import com.example.clubservice.domain.repo.ClubRepo;
import com.example.clubservice.presentation.dto.res.ClubListRes;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClubServiceImpl implements ClubService{

    private final ClubRepo clubRepo;

    @Override
    public List<ClubListRes> findAllByCategory(ClubCategory category) {
        List<Club> clubs = clubRepo.findAllByCategory(category);

        if (clubs.isEmpty()) {
            throw new EntityNotFoundException(category + " 카테고리에 해당하는 동아리가 없습니다.");
        }

        return clubs.stream()
                .map(ClubListRes::from)
                .toList();
    }

    @Override
    public void deleteClub(Long id) {
        if (!clubRepo.existsById(id)) {
            throw new EntityNotFoundException(id + "번에 해당하는 동아리를 찾을 수 없습니다.");
        }
        clubRepo.deleteById(id);
    }
}
