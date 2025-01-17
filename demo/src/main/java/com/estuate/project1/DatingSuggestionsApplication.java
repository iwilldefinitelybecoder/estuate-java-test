package com.estuate.project1;


import com.estuate.project1.model.Users;
import com.estuate.project1.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.util.List;

@SpringBootApplication
@EnableSpringDataWebSupport
public class DatingSuggestionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatingSuggestionsApplication.class, args);
	}

	/**
	 * This CommandLineRunner runs after Spring Boot starts.
	 * It checks if there are any UserEntity records in the database.
	 * If not, it seeds some sample data.
	 */
	@Bean
	public CommandLineRunner loadData(UserRepository userRepository) {
		return args -> {
			if (userRepository.count() == 0) {
				userRepository.saveAll(List.of(
						Users.builder()
								.name("User1")
								.gender("Female")
								.age(25)
								.interests(List.of("Cricket", "Chess"))
								.location("New York")
								.email("user1@example.com")

								.ageRangePreference(List.of(24, 30)) // Added ageRangePreference
								.build(),

						Users.builder()
								.name("User2")
								.gender("Male")
								.age(27)
								.interests(List.of("Cricket", "Football", "Movies"))
								.location("Los Angeles")
								.email("user2@example.com")

								.ageRangePreference(List.of(25, 35))
								.build(),

						Users.builder()
								.name("User3")
								.gender("Male")
								.age(26)
								.interests(List.of("Movies", "Tennis", "Football", "Cricket"))
								.location("Chicago")
								.email("user3@example.com")

								.ageRangePreference(List.of(22, 30))
								.build(),

						Users.builder()
								.name("User4")
								.gender("Female")
								.age(24)
								.interests(List.of("Tennis", "Football", "Badminton"))
								.location("San Francisco")
								.email("user4@example.com")

								.ageRangePreference(List.of(23, 28))
								.build(),

						Users.builder()
								.name("User5")
								.gender("Female")
								.age(32)
								.interests(List.of("Cricket", "Football", "Movies", "Badminton"))
								.location("Seattle")
								.email("user5@example.com")

								.ageRangePreference(List.of(30, 40))
								.build()
				));
			}
		};
	}
}