package com.example.clubservice.controller;

import com.example.clubservice.dto.req.ClubUpdateReq;
import com.example.clubservice.dto.res.ClubRes;
import com.example.clubservice.enums.ClubCategory;
import com.example.clubservice.response.Response;
import com.example.clubservice.dto.res.ClubListRes;
import com.example.clubservice.service.ClubService;
import lombok.AllArgsConstructor;
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

    @PutMapping("/{id}")
    public Response<Void> updateClub(
            @PathVariable Long id,
            @RequestBody ClubUpdateReq updateReq
    ) {
        clubService.updateClub(id, updateReq);
        return Response.ok("성공적으로 업데이트 되었습니다.");
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteClub(@PathVariable Long id) {
        clubService.deleteClub(id);

        return Response.ok("성공적으로 삭제되었습니다.");
    }
}
