package com.clipcoins.api.service;

import com.clipcoins.api.model.User;
import com.clipcoins.api.repository.UserRepository;
import com.clipcoins.api.utils.JwtUtil;
import com.clipcoins.api.utils.TokenGenerator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.repository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(long id) {
        return repository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User getUserByTelegramId(long telegramId) {
        return repository.findByTelegramId(telegramId);
    }

    public User getUserByToken(String token) {
        return repository.findByToken(token);
    }

    public String addUser(@Valid User user) {
        if (getUserById(user.getId()) != null) {
            return null;
        }

        // Generate token for user
        String generatedToken = TokenGenerator.generateToken();
        user.setToken(generatedToken);
        user.setTokenGeneratedAt(OffsetDateTime.now());

        repository.save(user);

        return jwtUtil.generateToken(user);
    }

    // TODO: optimize the code, remove duplicates,
    public ResponseEntity<?> updateUser(User user) {
        if (!userExists(user.getId(), user.getTelegramId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "User does not exist"));
        }

        if (user.getUsername() == null && user.getHashedCode() == null && user.getRole() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("error", "Nothing to update"));
        }

        User oldUser = getUserById(user.getId());

        if (user.getUsername() != null) {
            if (user.getUsername().isEmpty() || user.getUsername().isBlank()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("error", "Username cannot be empty"));
            }
            if (user.getUsername().equals(oldUser.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("error", "Username same as old"));
            }
            oldUser.setUsername(user.getUsername());
        }


        if (user.getHashedCode() != null) {
            if (user.getHashedCode().isEmpty() || user.getHashedCode().isBlank()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("error", "Hashcode cannot be empty"));
            }
            if (user.getHashedCode().equals(oldUser.getHashedCode())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("error", "H*+ashcode same as old"));
            }
            oldUser.setHashedCode(user.getHashedCode());
        }

        if (user.getRole() != null) {
            if (!List.of("USER", "ADMIN").contains(user.getRole())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("error", "User role must be USER or ADMIN"));
            }
            if (user.getRole().equals(oldUser.getRole())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("error", "User role same as old"));
            }
            oldUser.setRole(user.getRole());
        }

        oldUser.setUpdatedAt(OffsetDateTime.now());
        repository.save(oldUser);
        return ResponseEntity.ok(oldUser);
    }

    public User deleteUser(Long id) {
        User user = this.getUserById(id);

        if (user == null) return null;

        repository.delete(user);
        return user;
    }

    public boolean userExists(Long id, Long telegramId) {
        return repository.findById(id).isPresent() || repository.findByTelegramId(telegramId) != null;
    }
}
