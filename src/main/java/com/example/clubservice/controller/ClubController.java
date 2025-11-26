package com.example.clubservice.controller;

import com.example.clubservice.dto.request.ClubCreateRequest;
import com.example.clubservice.dto.request.ClubUpdateRequest;
import com.example.clubservice.dto.response.ClubResponse;
import com.example.clubservice.enums.ClubCategory;
import com.example.clubservice.dto.response.Response;
import com.example.clubservice.dto.response.ClubListResponse;
import com.example.clubservice.service.ClubService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/club")
@AllArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping("/create")
    public Response<String> createClub(@Valid @RequestBody ClubCreateRequest request) {
        clubService.createClub(request);
        return Response.created("정상적으로 생성되었습니다.");
    }

    @PutMapping("/{id}")
    public Response<Void> updateClub(
            @PathVariable Long id,
            @Valid @RequestBody ClubUpdateRequest updateRequest
    ) {
        clubService.updateClub(id, updateRequest);
        return Response.ok("성공적으로 업데이트 되었습니다.");
    }

    @GetMapping("/list")
    public Response<List<ClubListResponse>> getAllClubs() {
        List<ClubListResponse> response = clubService.getAllClubs();
        return Response.ok(response);
    }

    @GetMapping("/{id}")
    public Response<ClubResponse> getClub(@PathVariable Long id) {
        ClubResponse response = clubService.getClubById(id);
        return Response.ok(response);
    }

    @GetMapping("/category/{category}")
    public Response<List<ClubListResponse>> getClubsByCategory(@PathVariable ClubCategory category) {
        List<ClubListResponse> responses = clubService.findAllByCategory(category);

        return Response.ok(responses);
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteClub(@PathVariable Long id) {
        clubService.deleteClub(id);

        return Response.ok("성공적으로 삭제되었습니다.");
    }

    @GetMapping("/excel")
    public Response<Void> downloadClubExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=clubs.xlsx");

        clubService.writeClubExcel(response.getOutputStream());

        return Response.ok("다운로드 되었습니다.");
    }
}
