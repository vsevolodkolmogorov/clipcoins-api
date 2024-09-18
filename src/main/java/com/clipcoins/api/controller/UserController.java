package com.clipcoins.api.controller;

import com.clipcoins.api.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers() {
        return null;
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        return null;
    }

    @GetMapping()
    public ResponseEntity<User> getUserByTelegramId(@RequestParam String telegramId) {
        return null;
    }

    @GetMapping()
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        return null;
    }

    @PostMapping()
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return null;
    }

    @PutMapping()
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return null;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        return null;
    }
}
