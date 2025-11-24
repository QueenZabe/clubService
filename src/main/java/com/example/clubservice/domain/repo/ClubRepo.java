package com.example.clubservice.domain.repo;

import com.example.clubservice.domain.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepo extends JpaRepository<Club, Long> {
}
