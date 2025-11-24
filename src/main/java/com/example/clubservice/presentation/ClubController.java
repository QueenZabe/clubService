package com.example.clubservice.presentation;

import com.example.clubservice.domain.enums.ClubCategory;
import com.example.clubservice.presentation.dto.res.ClubListRes;
import com.example.clubservice.service.ClubService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs")
@AllArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @GetMapping("/{category}")
    public ResponseEntity<List<ClubListRes>> getClubsByCategory(@PathVariable ClubCategory category) {
        List<ClubListRes> clubs = clubService.findAllByCategory(category);

        return ResponseEntity.ok(clubs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClub(@PathVariable Long id) {
        clubService.deleteClub(id);

        return ResponseEntity.ok("성공적으로 삭제되었습니다.");
    }
}
