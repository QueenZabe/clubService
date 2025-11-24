package com.example.clubservice.domain;

import com.example.clubservice.domain.enums.ClubCategory;
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
