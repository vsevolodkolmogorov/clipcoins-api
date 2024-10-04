package com.clipcoins.api.service;

import com.clipcoins.api.model.Post;
import com.clipcoins.api.repository.PostRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class PostService {
    private final PostRepository repository;

    PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> getAllPosts() {
        return repository.findAll();
    }

    public Post getPostById(long id) {
        return repository.findById(id).orElse(null);
    }

    public Post addPost(@Valid Post post) {
        if (getPostById(post.getId()) != null) {
            return null;
        }

        return repository.save(post);
    }

    public ResponseEntity<?> updatePost(Post post) {
        if (getPostById(post.getId()) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Post does not exist"));
        }

        Post oldPost = getPostById(post.getId());

        if (post.getTitle() != null) {
            if (post.getTitle().isEmpty() || post.getTitle().isBlank()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("error", "Title cannot be empty"));
            }
            oldPost.setTitle(post.getTitle());
        }

        if (post.getImageUrl() != null) {
            if (post.getImageUrl().isEmpty() || post.getImageUrl().isBlank()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("error", "ImageUrl cannot be empty"));
            }
            oldPost.setImageUrl(post.getImageUrl());
        }

        if (post.getContent() != null) {
            if (post.getContent().isEmpty() || post.getContent().isBlank()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("error", "Content cannot be empty"));
            }
            oldPost.setContent(post.getContent());
        }

        oldPost.setUpdatedAt(OffsetDateTime.now());
        repository.save(oldPost);
        return ResponseEntity.ok(oldPost);
    }

    public Post deletePost(Long id) {
        Post post = this.getPostById(id);

        if (post == null) return null;

        repository.delete(post);
        return post;
    }
}
