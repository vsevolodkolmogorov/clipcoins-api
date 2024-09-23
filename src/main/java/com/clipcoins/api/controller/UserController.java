package com.clipcoins.api.controller;

import com.clipcoins.api.model.User;
import com.clipcoins.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService userService) {
        this.service = userService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = service.getAllUsers();

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Collections.singletonMap("error", "User list is empty"));
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        try {
            User user = service.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getByTelegramId/{telegramId}")
    public ResponseEntity<?> getUserByTelegramId(@PathVariable long telegramId) {
        User user = service.getUserByTelegramId(telegramId);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "User with telegramId " + telegramId + " not exist"));
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/getByName")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        User user = service.getUserByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "User with name " + username + " not exist"));
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping()
    public ResponseEntity<?> addUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        // Validation of the user by annotation from the model
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        if (service.userExists(user.getId(), user.getTelegramId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("error", "User already exists"));
        }

        User createdUser = service.addUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping()
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser = service.updateUser(user);

        if (updatedUser == null || updatedUser.equals(user)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        User deletedUser = service.deleteUser(id);

        if (deletedUser == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(deletedUser);
    }
}
