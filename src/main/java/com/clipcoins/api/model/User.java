package com.clipcoins.api.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue
    private long id;

    private long telegramId;
    private String username;
    private String hashedCode;
    private String role;
    private OffsetDateTime createdAt;
    private OffsetDateTime  updatedAt;

    public User() {}

    public User(long id, String username, String hashedCode) {
        this.id = id;
        this.username = username;
        this.hashedCode = hashedCode;
        this.role = "USER";
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(long telegramId) {
        this.telegramId = telegramId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedCode() {
        return hashedCode;
    }

    public void setHashedCode(String hashedCode) {
        this.hashedCode = hashedCode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdated_at(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && telegramId == user.telegramId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, telegramId);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", telegramId=" + telegramId +
                ", username='" + username + '\'' +
                ", hashedCode='" + hashedCode + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
