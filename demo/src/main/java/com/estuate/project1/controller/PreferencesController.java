package com.estuate.project1.controller;

import com.estuate.project1.model.Users;
import com.estuate.project1.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/preferences")
public class PreferencesController {

    private final UserService userService;

    public PreferencesController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Fetch user preferences.
     */
    @GetMapping
    public ResponseEntity<?> getPreferences() {
        try {
            Users user = userService.getUserPreferences();
            if (user == null) {
                return ResponseEntity.status(404).body("User not found.");
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching preferences: " + e.getMessage());
        }
    }

    /**
     * Add new user preferences.
     */
    @PostMapping
    public ResponseEntity<?> addPreferences(@RequestBody Map<String, Object> preferences) {
        try {
            Users updatedUser = userService.addPreferences(preferences);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding preferences: " + e.getMessage());
        }
    }

    /**
     * Update existing user preferences.
     */
    @PostMapping("/update")
    public ResponseEntity<?> updatePreferences(@RequestBody Map<String, Object> preferences) {
        try {
            Users updatedUser = userService.updatePreferences(preferences);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating preferences: " + e.getMessage());
        }
    }
}
