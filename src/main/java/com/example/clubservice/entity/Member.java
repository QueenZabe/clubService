package com.example.clubservice.entity;

import com.example.clubservice.enums.Authority;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "tb_member")
@Getter
@SuperBuilder
@NoArgsConstructor
public class Member {

    @Id
    private String phone;

    @Column
    private String name;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Authority authority = Authority.ROLE_USER;
}
