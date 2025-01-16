package com.estuate.project1.service;

import com.estuate.project1.model.Users;
import com.estuate.project1.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class MatchingService {
    private final UserRepository userRepository;


    public List<Users> getTopMatches(Long userId, int topN) {
        Users currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null) {
            return Collections.emptyList();
        }

        // Get all other users from DB
        List<Users> potentialMatches = userRepository.findAll();
        potentialMatches.removeIf(u -> u.getId().equals(currentUser.getId()));

        // Sort them by matching logic
        potentialMatches.sort((u1, u2) -> compareMatches(u1, u2, currentUser));

        // Return top N
        return potentialMatches.subList(0, Math.min(topN, potentialMatches.size()));
    }

    private int compareMatches(Users user1, Users user2, Users currentUser) {
        // 1) Opposite gender
        boolean user1Opposite = !user1.getGender().equalsIgnoreCase(currentUser.getGender());
        boolean user2Opposite = !user2.getGender().equalsIgnoreCase(currentUser.getGender());

        if (user1Opposite && !user2Opposite) return -1;
        if (!user1Opposite && user2Opposite) return 1;

        // 2) Among the same category (both opposite or both same), compare age difference
        int ageDiff1 = Math.abs(user1.getAge() - currentUser.getAge());
        int ageDiff2 = Math.abs(user2.getAge() - currentUser.getAge());
        if (ageDiff1 != ageDiff2) {
            return Integer.compare(ageDiff1, ageDiff2); // smaller is better
        }

        // 3) If still tied, compare number of overlapping interests (descending => bigger is better)
        int overlap1 = getInterestOverlap(user1, currentUser);
        int overlap2 = getInterestOverlap(user2, currentUser);
        return Integer.compare(overlap2, overlap1);
    }

    private int getInterestOverlap(Users user1, Users user2) {
        Set<String> set1 = new HashSet<>(user1.getInterests());
        set1.retainAll(user2.getInterests());
        return set1.size();
    }
}
