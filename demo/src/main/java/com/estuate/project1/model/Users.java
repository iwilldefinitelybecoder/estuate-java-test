package com.estuate.project1.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "users")  // safer than "user" (reserved in some SQL dialects)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String email;
    private String gender;
    private int age;
    private String imageUrl;
    private String location;

    @ElementCollection
    @CollectionTable(name = "user_interests", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "interest")
    private List<String> interests = new ArrayList<>();

    /**
     * For swipe logic, track the IDs of users you liked.
     * You could also store "super likes" separately or unify them in the same structure with a marker.
     */
    @ElementCollection
    @CollectionTable(name = "liked_users", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "liked_user_id")
    private List<Long> likedUsers = new ArrayList<>();

    /**
     * Could store matched users for quick reference.
     * A match occurs if both users like each other (like-lists overlap).
     */
    @ElementCollection
    @CollectionTable(name = "matched_users", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "matched_user_id")
    private List<Long> matchedUsers = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> ageRangePreference = Arrays.asList(20, 30); // Default range

}
