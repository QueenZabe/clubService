package com.example.clubservice.controller;

import com.example.clubservice.dto.request.ClubCreateRequest;
import com.example.clubservice.dto.request.ClubUpdateRequest;
import com.example.clubservice.enums.ClubCategory;
import com.example.clubservice.response.Response;
import com.example.clubservice.dto.response.ClubListResponse;
import com.example.clubservice.service.ClubService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/clubs")
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

    @GetMapping("/clubs/excel")
    public void downloadClubExcel(HttpServletResponse response) throws IOException {
        // 응답 헤더에 엑셀 파일 다운로드 설정
        response.setContentType("application/vnd.ms-excel"); // 엑셀 MIME 타입
        response.setHeader("Content-Disposition", "attachment; filename=clubs.xlsx"); // 다운로드 파일 이름 지정

        // 서비스에서 엑셀 워크북 생성 후 HTTP 응답 스트림으로 전달
        clubService.writeClubExcel(response.getOutputStream()); // OutputStream에 엑셀 데이터 작성
    }
}
