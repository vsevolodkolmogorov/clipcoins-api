package com.clipcoins.api.controller;

import com.clipcoins.api.model.User;
import com.clipcoins.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    public UserController(UserService userService) {
        this.service = userService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = service.getAllUsers();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        User user = service.getUserById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/getByTelegramId")
    public ResponseEntity<User> getUserByTelegramId(@RequestParam long telegramId) {
        User user = service.getUserByTelegramId(telegramId);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/getByName")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        User user = service.getUserByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping()
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User createdUser = service.addUser(user);

        if (createdUser == null || createdUser.getId() == 0) {
            return ResponseEntity.badRequest().build();
        }

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
