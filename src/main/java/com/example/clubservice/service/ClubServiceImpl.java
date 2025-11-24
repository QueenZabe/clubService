package com.example.clubservice.service;

import com.example.clubservice.domain.repo.ClubRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClubServiceImpl implements ClubService{

    private final ClubRepo clubRepo;

    public ResponseEntity<String> deleteClub(Long id) {
        if (!clubRepo.existsById(id)) {
            throw new EntityNotFoundException(id + "번에 해당하는 동아리를 찾을 수 없습니다.");
        }

        clubRepo.deleteById(id);
        return ResponseEntity.ok("성공적으로 삭제되었습니다.");
    }
}
