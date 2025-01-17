package com.estuate.project1.controller;

import com.estuate.project1.model.Users;
import com.estuate.project1.service.MatchingService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matching")
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    /**
     * Get paginated matches for a user.
     *
     * @param userId User ID
     * @param page   Page number
     * @param size   Page size
     * @return Paginated list of matches
     */
    @GetMapping("/matches")
    public ResponseEntity<?> getMatches(
            @RequestParam("userId") String userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        try {
            Page<Users> matches = matchingService.getTopMatches(Long.parseLong(userId), page, size);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching matches: " + e.getMessage());
        }
    }
}
