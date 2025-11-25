package com.example.clubservice.dto.res;

import com.example.clubservice.entity.Club;
import com.example.clubservice.enums.ClubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClubListRes {
    private Long id;
    private String name;
    private String category;
    private String description;

    public static ClubListRes from(Club club) {
        return new ClubListRes(
                club.getId(),
                club.getName(),
                club.getCategory().name(),
                club.getDescription()
        );
    }
}
