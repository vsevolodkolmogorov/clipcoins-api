package com.clipcoins.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue
    private long id;

    private long telegram_id;
    private String username;
    private String hashed_code;
    private String role;
    private OffsetDateTime created_at;
    private OffsetDateTime  updated_at;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTelegram_id() {
        return telegram_id;
    }

    public void setTelegram_id(long telegram_id) {
        this.telegram_id = telegram_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashed_code() {
        return hashed_code;
    }

    public void setHashed_code(String hashed_code) {
        this.hashed_code = hashed_code;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public OffsetDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(OffsetDateTime created_at) {
        this.created_at = created_at;
    }

    public OffsetDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(OffsetDateTime updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && telegram_id == user.telegram_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, telegram_id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", telegram_id=" + telegram_id +
                ", username='" + username + '\'' +
                ", hashed_code='" + hashed_code + '\'' +
                ", role='" + role + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
