package com.clipcoins.api.controller;

import com.clipcoins.api.model.Post;
import com.clipcoins.api.service.PostService;
import com.clipcoins.api.utils.ValidationUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        List<Post> posts = service.getAllPosts();

        if (posts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Collections.singletonMap("error", "Posts list is empty"));
        }

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable long id) {
        Post post = service.getPostById(id);

        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Post with id " + id + " not exist"));
        }

        return ResponseEntity.ok(post);
    }

    @PostMapping()
    public ResponseEntity<?> addPost(@Valid @RequestBody Post post, BindingResult bindingResult) {
        ResponseEntity<?> errors = ValidationUtil.checkParam(bindingResult);
        if (errors != null) return errors;

        if (service.addPost(post) == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("error", "Post already exists"));
        }

        return ResponseEntity.ok(post);
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody Post post) {
        return service.updatePost(post);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePost(@PathVariable long id) {
        Post deletedPost = service.deletePost(id);

        if (deletedPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Post does not exist"));
        }

        return ResponseEntity.ok(deletedPost);
    }
}
