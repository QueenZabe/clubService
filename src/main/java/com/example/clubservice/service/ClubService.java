package com.example.clubservice.service;

import com.example.clubservice.domain.repo.ClubRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClubService {

    private final ClubRepo clubRepo;

    public ResponseEntity<String> deleteClub(Long id) {
        try {
            if (!clubRepo.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(id + "번 에 해당하는 동아리를 찾을 수 없습니다.");
            }

            clubRepo.deleteById(id);
            return ResponseEntity.ok("성공적으로 삭제되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
