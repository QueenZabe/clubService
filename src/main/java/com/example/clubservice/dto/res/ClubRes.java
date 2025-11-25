package com.example.clubservice.dto.res;

import com.example.clubservice.entity.Club;
import com.example.clubservice.enums.ClubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClubRes {
    private Long id;
    private String name;
    private String description;
    private ClubCategory category;

    public static ClubRes from(Club club) {
        return new ClubRes(
                club.getId(),
                club.getName(),
                club.getDescription(),
                club.getCategory()
        );
    }
}