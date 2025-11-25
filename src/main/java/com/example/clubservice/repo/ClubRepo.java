package com.example.clubservice.repo;

import com.example.clubservice.entity.Club;
import com.example.clubservice.enums.ClubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepo extends JpaRepository<Club, Long> {
    List<Club> findAllByCategory(ClubCategory category);

    boolean existsByName(String name);

    List<Club> findAllOrderByCreatedAtDesc();
}
