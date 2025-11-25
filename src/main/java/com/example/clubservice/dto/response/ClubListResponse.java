package com.example.clubservice.dto.response;

import com.example.clubservice.entity.Club;
import com.example.clubservice.enums.ClubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClubListResponse {
    private Long id;
    private String name;
    private ClubCategory category;

    public static ClubListResponse from(Club club) {
        return new ClubListResponse(
                club.getId(),
                club.getName(),
                club.getCategory()
        );
    }
}
