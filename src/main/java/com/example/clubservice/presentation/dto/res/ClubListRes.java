package com.example.clubservice.presentation.dto.res;

import com.example.clubservice.domain.Club;
import com.example.clubservice.domain.enums.ClubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClubListRes {
    private Long id;
    private String name;
    private ClubCategory category;

    public static ClubListRes from(Club club) {
        return new ClubListRes(
                club.getId(),
                club.getName(),
                club.getCategory()
        );
    }
}
