package com.example.clubservice.dto.request;

import com.example.clubservice.enums.ClubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClubCreateRequest {
    private String name;
    private String description;
    private ClubCategory category;
}