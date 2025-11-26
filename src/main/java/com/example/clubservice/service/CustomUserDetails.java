package com.example.clubservice.service;

import com.example.clubservice.entity.Member;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails {
    UserDetails loadUserByUsername(String phone);
    UserDetails createUserDetails(Member member);
}
