package com.estuate.project1;

import com.estuate.project1.model.Users;
import com.estuate.project1.repository.UserRepository;
import com.estuate.project1.service.MatchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class MatchingServiceTest {

    private MatchingService matchingService;
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        matchingService = new MatchingService(userRepository);
    }

    @Test
    public void testGetTopMatches_OppositeGenderFiltering() {
        Users currentUser = createUser(1L, "User1", "Male", 25, List.of("Movies"), "New York");
        Users user2 = createUser(2L, "User2", "Female", 24, List.of("Movies"), "New York");
        Users user3 = createUser(3L, "User3", "Male", 26, List.of("Movies"), "Los Angeles");

        when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
        when(userRepository.findAll()).thenReturn(List.of(currentUser, user2, user3));

        Page<Users> matches = matchingService.getTopMatches(1L, 0, 10);

        assertThat(matches.getContent()).containsExactly(user2);
    }

    @Test
    public void testGetTopMatches_AgeRangeFiltering() {
        Users currentUser = createUser(1L, "User1", "Male", 25, List.of("Movies"), "New York", List.of(20, 30));
        Users user2 = createUser(2L, "User2", "Female", 24, List.of("Movies"), "New York");
        Users user3 = createUser(3L, "User3", "Female", 35, List.of("Movies"), "Los Angeles");

        when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
        when(userRepository.findAll()).thenReturn(List.of(currentUser, user2, user3));

        Page<Users> matches = matchingService.getTopMatches(1L, 0, 10);

        assertThat(matches.getContent()).containsExactly(user2);
    }

    @Test
    public void testGetTopMatches_InterestFiltering() {
        Users currentUser = createUser(1L, "User1", "Male", 25, List.of("Movies", "Sports"), "New York");
        Users user2 = createUser(2L, "User2", "Female", 24, List.of("Movies"), "New York");
        Users user3 = createUser(3L, "User3", "Female", 26, List.of("Cooking"), "Los Angeles");

        when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
        when(userRepository.findAll()).thenReturn(List.of(currentUser, user2, user3));

        Page<Users> matches = matchingService.getTopMatches(1L, 0, 10);

        assertThat(matches.getContent()).containsExactly(user2);
    }

    @Test
    public void testGetTopMatches_Pagination() {
        Users currentUser = createUser(1L, "User1", "Male", 25, List.of("Movies"), "New York");
        Users user2 = createUser(2L, "User2", "Female", 24, List.of("Movies"), "New York");
        Users user3 = createUser(3L, "User3", "Female", 26, List.of("Movies"), "Los Angeles");
        Users user4 = createUser(4L, "User4", "Female", 28, List.of("Movies"), "Chicago");

        when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
        when(userRepository.findAll()).thenReturn(List.of(currentUser, user2, user3, user4));

        Page<Users> matches = matchingService.getTopMatches(1L, 0, 2);

        assertThat(matches.getContent()).containsExactly(user2, user3);
        assertThat(matches.getTotalElements()).isEqualTo(3);
        assertThat(matches.getTotalPages()).isEqualTo(2);
    }

    private Users createUser(Long id, String name, String gender, int age, List<String> interests, String location) {
        return createUser(id, name, gender, age, interests, location, List.of(20, 30));
    }

    private Users createUser(Long id, String name, String gender, int age, List<String> interests, String location, List<Integer> ageRangePreference) {
        Users user = new Users();
        user.setId(id);
        user.setName(name);
        user.setGender(gender);
        user.setAge(age);
        user.setInterests(interests);
        user.setLocation(location);
        user.setAgeRangePreference(ageRangePreference);
        return user;
    }
}
