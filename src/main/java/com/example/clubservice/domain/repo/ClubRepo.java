package com.example.clubservice.domain.repo;

import com.example.clubservice.domain.Club;
import com.example.clubservice.domain.enums.ClubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepo extends JpaRepository<Club, Long> {
    List<Club> findAllByCategory(ClubCategory category);
}
