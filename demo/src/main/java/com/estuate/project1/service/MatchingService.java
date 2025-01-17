package com.estuate.project1.service;

import com.estuate.project1.model.Users;
import com.estuate.project1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final UserRepository userRepository;

    public Page<Users> getTopMatches(Long userId, int page, int size) {
        Users currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null) {
            return Page.empty();
        }

        // Get all unique users from DB and remove the current user
        Set<Users> potentialMatches = new HashSet<>(userRepository.findAll());
        potentialMatches.removeIf(u -> u.getId().equals(currentUser.getId()));

        // Filter and sort them by matching logic
        List<Users> filteredMatches = potentialMatches.stream()
                .filter(u -> !u.getGender().equalsIgnoreCase(currentUser.getGender())) // Opposite gender
                .filter(u -> isWithinAgeRange(u.getAge(), currentUser.getAgeRangePreference())) // Age range check
                .filter(u -> getInterestOverlap(u, currentUser) > 0) // Ensure at least one common interest
                .distinct() // Ensure no duplicates
                .sorted((u1, u2) -> compareMatches(u1, u2, currentUser))
                .collect(Collectors.toList());

        // Paginate the results
        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), filteredMatches.size());
        int end = Math.min((start + pageable.getPageSize()), filteredMatches.size());

        return new PageImpl<>(filteredMatches.subList(start, end), pageable, filteredMatches.size());
    }

    private boolean isWithinAgeRange(int age, List<Integer> ageRange) {
        if (ageRange == null || ageRange.size() != 2) {
            return false; // Invalid or unset age range
        }
        return age >= ageRange.get(0) && age <= ageRange.get(1);
    }

    private int compareMatches(Users user1, Users user2, Users currentUser) {
        // 1) Compare number of overlapping interests (descending => bigger is better)
        int overlap1 = getInterestOverlap(user1, currentUser);
        int overlap2 = getInterestOverlap(user2, currentUser);
        if (overlap1 != overlap2) {
            return Integer.compare(overlap2, overlap1);
        }

        // 2) Compare age difference
        int ageDiff1 = Math.abs(user1.getAge() - currentUser.getAge());
        int ageDiff2 = Math.abs(user2.getAge() - currentUser.getAge());
        if (ageDiff1 != ageDiff2) {
            return Integer.compare(ageDiff1, ageDiff2); // smaller is better
        }

        // 3) Compare location match (priority given to matching location)
        boolean locationMatch1 = currentUser.getLocation().equalsIgnoreCase(user1.getLocation());
        boolean locationMatch2 = currentUser.getLocation().equalsIgnoreCase(user2.getLocation());
        if (locationMatch1 && !locationMatch2) {
            return -1; // user1 is better
        }
        if (!locationMatch1 && locationMatch2) {
            return 1; // user2 is better
        }

        // 4) Fallback comparison by ID to ensure consistent ordering
        return Long.compare(user1.getId(), user2.getId());
    }

    private int getInterestOverlap(Users user1, Users user2) {
        Set<String> set1 = new HashSet<>(user1.getInterests());
        set1.retainAll(user2.getInterests());
        return set1.size();
    }
}
