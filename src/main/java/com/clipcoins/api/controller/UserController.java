package com.clipcoins.api.controller;

import com.clipcoins.api.model.User;
import com.clipcoins.api.service.UserService;
import com.clipcoins.api.utils.ValidationUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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
    public ResponseEntity<?> getUserById(@PathVariable long id) {
        User user = service.getUserById(id);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "User with id " + id + " not exist"));
        }

        return ResponseEntity.ok(user);
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
        ResponseEntity<?> errors = ValidationUtil.checkUser(bindingResult);
        if (errors != null) return errors;

        String jwtToken = service.addUser(user);

        if (service.addUser(user) == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("error", "User already exists"));
        }

        return ResponseEntity.ok(jwtToken);
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        return service.updateUser(user);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        User deletedUser = service.deleteUser(id);

        if (deletedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "User does not exist"));
        }

        return ResponseEntity.ok(deletedUser);
    }

    // -------------------- LOGIN LOGIC --------------------

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username) {
        return service.loginUser(username);
    }

    @PostMapping("/login/verify")
    public ResponseEntity<?> verifyToken(@RequestParam String token) {
        return service.verifyToken(token);
    }

    @GetMapping("/auth")
    public ResponseEntity<?> auth(@RequestHeader("Authorization") String jwtTokenHeader) {
        return service.checkJwtToken(jwtTokenHeader);
    }
}
