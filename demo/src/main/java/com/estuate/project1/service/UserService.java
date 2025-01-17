package com.estuate.project1.service;

import com.estuate.project1.model.Users;
import com.estuate.project1.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Fetch the authenticated user from the SecurityContextHolder.
     */
    public Users getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User is not authenticated.");
        }

        String email = authentication.getName(); // Assumes email is stored as the principal
        return userRepository.findByEmail(email);
    }

    /**
     * Add or update user preferences.
     */
    public Users addPreferences(Map<String, Object> preferences) {
        Users user = getAuthenticatedUser(); // Fetches authenticated user
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        // Add preferences only if the user has none (initial setup)
        if (user.getInterests() == null || user.getInterests().isEmpty()) {
            updatePreferencesFields(user, preferences);
            return userRepository.save(user);
        }

        throw new IllegalArgumentException("Preferences already exist. Use the update endpoint.");
    }
    public Users updatePreferences(Map<String, Object> preferences) {
        Users user = getAuthenticatedUser(); // Fetches authenticated user
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        // Update preferences fields
        updatePreferencesFields(user, preferences);
        return userRepository.save(user);
    }



    private void updatePreferencesFields(Users user, Map<String, Object> preferences) {
        // Update age range preference
        if (preferences.containsKey("ageRange")) {
            List<Integer> ageRange = (List<Integer>) preferences.get("ageRange");
            if (ageRange != null && ageRange.size() == 2) {
                // Assuming the user entity has a List<Integer> ageRangePreference field
                user.setAgeRangePreference(ageRange);
            } else {
                throw new IllegalArgumentException("Age range must contain exactly two integers (min and max).");
            }
        }

        // Update interests
        if (preferences.containsKey("interests")) {
            List<String> interests = (List<String>) preferences.get("interests");
            user.setInterests(interests);
        }

        if (preferences.containsKey("age")) {
            int age = (int) preferences.get("age");
            user.setAge(age);
        }
        // Update location
        if (preferences.containsKey("location")) {
            user.setLocation(preferences.get("location").toString());
        }

        // Update gender
        if (preferences.containsKey("gender")) {
            user.setGender(preferences.get("gender").toString());
        }
    }

    public Users getUserPreferences() {
        Users user = getAuthenticatedUser();
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        return user;
    }


}
