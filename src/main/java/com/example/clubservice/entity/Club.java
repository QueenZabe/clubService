package com.example.clubservice.entity;

import com.example.clubservice.dto.request.ClubUpdateRequest;
import com.example.clubservice.enums.ClubCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name="tb_club")
@Getter
@SuperBuilder
@NoArgsConstructor
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private ClubCategory category;


    public void update(ClubUpdateRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.category = request.getCategory();
    }
}
