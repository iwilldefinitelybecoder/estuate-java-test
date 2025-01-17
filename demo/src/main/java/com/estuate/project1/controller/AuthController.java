package com.estuate.project1.controller;



import com.estuate.project1.DTO.AuthResponse;
import com.estuate.project1.model.Users;
import com.estuate.project1.repository.UserRepository;
import com.estuate.project1.utils.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import com.google.api.client.json.gson.GsonFactory;

import com.google.api.client.http.javanet.NetHttpTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {


    private String googleClientId = "727291520867-tp7mnfv8slhdqmkog0rtp2qole99o35b.apps.googleusercontent.com";

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Handles the ID token sent from the frontend, validates it, and returns a JWT.
     */
    @PostMapping("/google")
    public ResponseEntity<?> handleGoogleLogin(@RequestBody Map<String,String> idTokenString) {

        try {
            // 1) Verify the ID token

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), new GsonFactory()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();
            log.info(idTokenString.get("idToken"));
            GoogleIdToken idToken = verifier.verify(idTokenString.get("idToken"));
            if (idToken == null) {
                return ResponseEntity.badRequest().body("Invalid ID token.");
            }

            // 2) Extract user info from the ID token
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");

            // 3) Create or update the user in the database
            Users user = userRepository.findByEmail(email);
            if (user == null) {
                user = new Users();
                user.setEmail(email);
            }
            user.setName(name);

            user.setImageUrl(pictureUrl);
            userRepository.save(user);


            // 4) Generate our own JWT
            String jwt = jwtUtil.generateToken(user.getEmail());

            return ResponseEntity.ok(new AuthResponse(
                    jwt,
                    user.getEmail(),
                    user.getName(),
                    user.getImageUrl(),
                    user.getId(),
                    (user.getInterests() != null && !user.getInterests().isEmpty()) ? user.getInterests() : null
            ));

        } catch (Exception e) {
            log.error("Error during Google login: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Authentication failed: " + e.getMessage());
        }
    }
}
