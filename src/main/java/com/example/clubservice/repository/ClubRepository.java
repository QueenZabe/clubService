package com.example.clubservice.repository;

import com.example.clubservice.entity.Club;
import com.example.clubservice.enums.ClubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Club findByName(String name);
    boolean existsByName(String name);
    List<Club> findAllByOrderByCreatedAtDesc();
    List<Club> findAllByCategory(ClubCategory category);
}