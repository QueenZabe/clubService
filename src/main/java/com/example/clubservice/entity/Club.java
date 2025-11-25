package com.example.clubservice.entity;

import com.example.clubservice.dto.res.ClubRes;
import com.example.clubservice.enums.ClubCategory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tb_club")
@Getter
@NoArgsConstructor
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private ClubCategory category;

    @Builder
    public Club(String name, String description, ClubCategory category){
        this.name = name;
        this.description = description;
        this.category = category;
    }
}
