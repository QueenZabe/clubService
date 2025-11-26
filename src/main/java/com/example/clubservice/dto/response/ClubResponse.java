package com.example.clubservice.dto.response;

import com.example.clubservice.entity.Club;
import com.example.clubservice.enums.ClubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ClubResponse {
    private Long id;
    private String name;
    private String description;
    private ClubCategory category;
    private LocalDateTime createdAt;

    public static ClubResponse from(Club club) {
        return new ClubResponse(
                club.getId(),
                club.getName(),
                club.getDescription(),
                club.getCategory(),
                club.getCreatedAt()
        );
    }
}