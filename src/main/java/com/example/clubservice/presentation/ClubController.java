package com.example.clubservice.presentation;

import com.example.clubservice.dto.req.ClubCreateReq;
import com.example.clubservice.enums.ClubCategory;
import com.example.clubservice.response.Response;
import com.example.clubservice.dto.res.ClubListRes;
import com.example.clubservice.service.ClubService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs")
@AllArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @GetMapping("/{category}")
    public Response<List<ClubListRes>> getClubsByCategory(@PathVariable ClubCategory category) {
        List<ClubListRes> clubs = clubService.findAllByCategory(category);

        return Response.ok(clubs);
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteClub(@PathVariable Long id) {
        clubService.deleteClub(id);

        return Response.ok("성공적으로 삭제되었습니다.");
    }

    @PostMapping
    public ResponseEntity<Response<ClubListRes>> createClub(
            @Valid @RequestBody ClubCreateReq request) {
        ClubListRes response = clubService.createClub(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.created(response));
    }

    @GetMapping
    public ResponseEntity<Response<List<ClubListRes>>> getAllClubs() {
        List<ClubListRes> response = clubService.getAllClubs();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.ok(response));
    }
}
