package com.clipcoins.api.repository;

import com.clipcoins.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByTelegramId(Long telegramId);
    User findByUsername(String username);
    User findByToken(String token);
}
