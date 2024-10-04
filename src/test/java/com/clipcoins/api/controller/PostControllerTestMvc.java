package com.clipcoins.api.controller;

import com.clipcoins.api.model.Post;
import com.clipcoins.api.model.User;
import com.clipcoins.api.repository.PostRepository;
import com.clipcoins.api.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
public class PostControllerTestMvc {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostRepository repository;

    @SpyBean
    private PostService service;

    @InjectMocks
    private PostController controller;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------------- GET --------------------

    @Test
    public void testGetAllPostsNoContent() throws Exception {
        when(service.getAllPosts()).thenReturn(Collections.emptyList());

        mvc.perform(get( "/posts/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.error").value("Posts list is empty"));
    }

    @Test
    public void testGetAllPostsOk() throws Exception {
        User user = new User(1,"testUser");
        Post post = new Post("Test","test", user);

        List<Post> posts = List.of(post);

        when(service.getAllPosts()).thenReturn(posts);

        mvc.perform(get( "/posts/getAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Test"));
    }

    @Test
    public void testGetPostByIdWithNoContent() throws Exception {
        int postId = 1;

        when(service.getPostById(1L)).thenReturn(null);

        mvc.perform(get("/posts/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Post with id " + postId + " not exist"));
    }

    @Test
    public void testGetPostByIdOk() throws Exception {
        User testUser = new User( 1L, "user");
        Post post = new Post("Test","test", testUser);
        post.setId(1L);

        doReturn(post).when(service).getPostById(1L);


        mvc.perform(get("/posts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test"));
    }

    // ---------------- POST --------------------

    @Test
    public void testPostWithNoContent() throws Exception {
        Post testPost = new Post();

        mvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPost)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostWithInvalidId() throws Exception {
        User testUser = new User( 1L, "user");
        testUser.setId(1L);
        Post post = new Post("Test","test", testUser);
        post.setId(-1L);

        when(repository.save(any(Post.class))).thenReturn(post);

        mvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value("Id must be a positive number"));
    }

    @Test
    public void testPostWithNullTitle() throws Exception {
        User testUser = new User( 1L, "user");
        testUser.setId(1L);
        Post post = new Post(null,"test", testUser);

        when(repository.save(any(Post.class))).thenReturn(post);

        mvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title cannot be empty"));
    }

    @Test
    public void testPostWithNullContent() throws Exception {
        User testUser = new User( 1L, "user");
        testUser.setId(1L);
        Post post = new Post("Test",null, testUser);

        when(repository.save(any(Post.class))).thenReturn(post);

        mvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value("Content cannot be empty"));
    }

    @Test
    public void testPostAlreadyExisted() throws Exception {
        User testUser = new User( 1L, "user");
        testUser.setId(1L);
        Post post = new Post("Test","test", testUser);

        doReturn(post).when(service).getPostById(any(Long.class));

        mvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Post already exists"));
    }

    @Test
    public void testPostOk() throws Exception {
        User testUser = new User( 1L, "user");
        testUser.setId(1L);
        Post post = new Post("Test","test", testUser);

        when(repository.save(any(Post.class))).thenReturn(post);

        mvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk());
    }

    // ---------------- PUT --------------------

    @Test
    public void testPutPostWithNoExistedId() throws Exception {
        User testUser = new User( 1L, "user");
        testUser.setId(1L);
        Post post = new Post("Test","test", testUser);
        post.setId(Long.MAX_VALUE);

        mvc.perform(put("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Post does not exist"));
    }

    @Test
    public void testPutPostWithInvalidId() throws Exception {
        User testUser = new User( 1L, "user");
        testUser.setId(1L);
        Post post = new Post("Test","test", testUser);
        post.setId(-1L);

        when(repository.save(any(Post.class))).thenReturn(post);

        mvc.perform(put("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Post does not exist"));
    }

    @Test
    public void testPutPostOk() throws Exception {
        User testUser = new User( 1L, "user");
        testUser.setId(1L);

        Post oldPost = new Post("Test","test", testUser);
        Post newPost = new Post(null,"test updated", null);

        doReturn(oldPost).when(service).getPostById(any(Long.class));

        mvc.perform(put("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPost)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test"))
                .andExpect(jsonPath("$.content").value("test updated"));
    }

    // ---------------- DELETE --------------------

    @Test
    public void testDeletePostWithNoContent() throws Exception {
        long postId = Long.MAX_VALUE;

        mvc.perform(delete("/posts/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Post does not exist"));
    }

    @Test
    public void testDeletePostWithInvalidId() throws Exception {
        Long postId = -1L;

        mvc.perform(delete("/posts/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Post does not exist"));
    }

    @Test
    public void testDeletePostWithNonExistedId() throws Exception {
        Long postId = Long.MAX_VALUE;

        mvc.perform(delete("/posts/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Post does not exist"));
    }

    @Test
    public void testDeletePostOk() throws Exception {
        User testUser = new User(1L, "user");
        testUser.setId(1L);
        Post post = new Post("Test","test", testUser);

        doReturn(post).when(service).getPostById(any(Long.class));

        mvc.perform(delete("/posts/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
