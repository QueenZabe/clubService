package com.example.clubservice.dto.request;

import com.example.clubservice.enums.ClubCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ClubUpdateRequest {
    private String name;
    private String description;
    private ClubCategory category;
}
