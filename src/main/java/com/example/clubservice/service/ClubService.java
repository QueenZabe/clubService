package com.example.clubservice.service;

import org.springframework.http.ResponseEntity;

public interface ClubService {
    ResponseEntity<String> deleteClub(Long id);
}
