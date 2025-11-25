package com.example.clubservice.dto.req;

import com.example.clubservice.enums.ClubCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubUpdateReq {
    private String name;
    private String description;
    private ClubCategory category;
}
