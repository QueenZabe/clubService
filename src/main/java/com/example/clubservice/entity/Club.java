package com.example.clubservice.entity;

import com.example.clubservice.enums.ClubCategory;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name="tb_club")
@Getter
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
}
