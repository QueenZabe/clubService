package com.example.clubservice.controller;

import com.example.clubservice.dto.request.ClubCreateRequest;
import com.example.clubservice.dto.request.ClubUpdateRequest;
import com.example.clubservice.enums.ClubCategory;
import com.example.clubservice.dto.response.Response;
import com.example.clubservice.dto.response.ClubListResponse;
import com.example.clubservice.service.ClubService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/club")
@AllArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    public Response<String> createClub(@RequestBody ClubCreateRequest request) {
        clubService.createClub(request);
        return Response.created("정상적으로 생성되었습니다.");
    }

    @GetMapping
    public Response<List<ClubListResponse>> getAllClubs() {
        List<ClubListResponse> response = clubService.getAllClubs();
        return Response.ok(response);
    }

    @GetMapping("/{category}")
    public Response<List<ClubListResponse>> getClubsByCategory(@PathVariable ClubCategory category) {
        List<ClubListResponse> clubs = clubService.findAllByCategory(category);

        return Response.ok(clubs);
    }

    @PutMapping("/{id}")
    public Response<Void> updateClub(
            @PathVariable Long id,
            @RequestBody ClubUpdateRequest updateRequest
    ) {
        clubService.updateClub(id, updateRequest);
        return Response.ok("성공적으로 업데이트 되었습니다.");
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteClub(@PathVariable Long id) {
        clubService.deleteClub(id);

        return Response.ok("성공적으로 삭제되었습니다.");
    }
}
