package com.clipcoins.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id cannot be null")
    @Min(value = 0, message = "Id must be a positive number")
    private long id;

    @NotNull(message = "TelegramId cannot be null")
    @Min(value = 0, message = "TelegramId must be a positive number")
    private long telegramId;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    private String token;

    @NotBlank(message = "Role cannot be null")
    @Pattern(regexp = "USER|ADMIN", message = "Role must be either USER or ADMIN")
    private String role;

    @CreationTimestamp
    @PastOrPresent(message = "CreatedAt must be in the past or present")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @PastOrPresent(message = "UpdatedAt must be in the past or present")
    private OffsetDateTime  updatedAt;

    @DateTimeFormat
    @PastOrPresent(message = "TokenGeneratedAt must be in the past or present")
    private OffsetDateTime  tokenGeneratedAt;

    @OneToMany(mappedBy = "post")
    private List<Post> posts;

    public User() {}

    public User(long telegramId, String username) {
        this.telegramId = telegramId;
        this.username = username;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OffsetDateTime getTokenGeneratedAt() {
        return tokenGeneratedAt;
    }

    public void setTokenGeneratedAt(OffsetDateTime tokenGeneratedAt) {
        this.tokenGeneratedAt = tokenGeneratedAt;
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
                ", token='" + token + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", tokenGeneratedAt=" + tokenGeneratedAt +
                '}';
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
