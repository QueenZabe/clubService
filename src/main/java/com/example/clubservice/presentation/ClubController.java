package com.example.clubservice.presentation;

import com.example.clubservice.service.ClubService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clubs")
@AllArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClub(@PathVariable("id") Long id) {
        return clubService.deleteClub(id);
    }
}
