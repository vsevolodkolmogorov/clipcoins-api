package com.clipcoins.api.service;

import com.clipcoins.api.model.User;
import com.clipcoins.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository userRepository) {
        this.repository = userRepository;
    }

    public List<User> getAllUsers() {
        return null;
    }

    public User getUserById(long id) {
        return null;
    }

    public User getUserByTelegramId(long id) {
        return null;
    }

    public User getUserByUsername(String username) {
        return null;
    }

    public User addUser(User user) {
        return null;
    }

    public User updateUser(User user) {
        return null;
    }

    public User deleteUser(Long id) {
        return null;
    }

}
