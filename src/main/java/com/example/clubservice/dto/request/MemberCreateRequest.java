package com.example.clubservice.dto.request;

import com.example.clubservice.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;

public record MemberCreateRequest(
        String phone,
        String name,
        String password
) {
    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .phone(phone)
                .name(name)
                .password(passwordEncoder.encode(password))
                .build();
    }
}
