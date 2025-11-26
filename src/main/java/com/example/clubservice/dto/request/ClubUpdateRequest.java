package com.example.clubservice.dto.request;

import com.example.clubservice.enums.ClubCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClubUpdateRequest {
    @NotBlank(message = "동아리명은 필수 입력입니다.") // null, 빈 문자열 체크
    private String name;

    @NotBlank(message = "설명은 필수 입력입니다.")
    private String description;

    @NotNull(message = "카테고리는 필수 선택입니다.")
    private ClubCategory category;
}
