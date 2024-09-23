package com.clipcoins.api.service;

import com.clipcoins.api.model.User;
import com.clipcoins.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository userRepository) {
        this.repository = userRepository;
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

    public User addUser(@Valid User user) {
        return repository.save(user);
    }

    public User updateUser(User user) {
        return repository.save(user);
    }

    public User deleteUser(Long id) {
        return null;
    }

    public boolean userExists(Long id, Long telegramId) {
        return repository.findById(id).isPresent() ||
                repository.findByTelegramId(telegramId) != null;
    }
}
