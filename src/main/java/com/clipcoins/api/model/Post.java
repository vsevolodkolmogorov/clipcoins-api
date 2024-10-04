package com.clipcoins.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id cannot be null")
    @Min(value = 0, message = "Id must be a positive number")
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    @NotNull
    private User user;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Content cannot be empty")
    private String content;

    private String imageUrl;

    @Min(value = 0, message = "LikesCount must be a positive number")
    private Integer likesCount;

    @Min(value = 0, message = "DislikesCount must be a positive number")
    private Integer dislikesCount;

    @Min(value = 0, message = "ViewsCount must be a positive number")
    private Integer viewsCount;

    @CreationTimestamp
    @PastOrPresent(message = "CreatedAt must be in the past or present")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @PastOrPresent(message = "UpdatedAt must be in the past or present")
    private OffsetDateTime  updatedAt;

    public Post() {}

    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.likesCount = 0;
        this.dislikesCount = 0;
        this.viewsCount = 0;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Title cannot be empty") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title cannot be empty") String title) {
        this.title = title;
    }

    public @NotBlank(message = "Content cannot be empty") String getContent() {
        return content;
    }

    public void setContent(@NotBlank(message = "Content cannot be empty") String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public @Min(value = 0, message = "LikesCount must be a positive number") Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(@Min(value = 0, message = "LikesCount must be a positive number") Integer likesCount) {
        this.likesCount = likesCount;
    }

    public @Min(value = 0, message = "DislikesCount must be a positive number") Integer getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(@Min(value = 0, message = "DislikesCount must be a positive number") Integer dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

    public @Min(value = 0, message = "ViewsCount must be a positive number") Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(@Min(value = 0, message = "ViewsCount must be a positive number") Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public @PastOrPresent(message = "CreatedAt must be in the past or present") OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@PastOrPresent(message = "CreatedAt must be in the past or present") OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public @PastOrPresent(message = "UpdatedAt must be in the past or present") OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(@PastOrPresent(message = "UpdatedAt must be in the past or present") OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", likesCount=" + likesCount +
                ", dislikesCount=" + dislikesCount +
                ", viewsCount=" + viewsCount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
