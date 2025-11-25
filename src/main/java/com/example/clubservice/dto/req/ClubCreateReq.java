package com.example.clubservice.dto.req;

import com.example.clubservice.enums.ClubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClubCreateReq {
    private String name;
    private String description;
    private ClubCategory category;
}
