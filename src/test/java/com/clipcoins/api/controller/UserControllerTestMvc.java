package com.clipcoins.api.controller;

import com.clipcoins.api.model.User;
import com.clipcoins.api.repository.UserRepository;
import com.clipcoins.api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTestMvc {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository repository;

    @SpyBean
    private UserService service;

    @InjectMocks
    private UserController controller;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllUsersWithNoContent() throws Exception {
        when(service.getAllUsers()).thenReturn(Collections.emptyList());

        mvc.perform(get("/users/getAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetAllUsersOk() throws Exception {
        List<User> testUsers = List.of(
                new User(1L,1L, "user1"),
                new User(2L,2L, "user2"),
                new User(3L,3L, "user3" )
        );

        when(service.getAllUsers()).thenReturn(testUsers);

        mvc.perform(get("/users/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(testUsers.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].username").value("user2"))
                .andExpect(jsonPath("$[2].hashedCode").value("hashedCode3"));
    }

    @Test
    public void testGetUserByIdWithNoContent() throws Exception {
        int userId = 1;

        doThrow(new EntityNotFoundException("User with id " + userId + " not found"))
                .when(service).getUserById(userId);

        mvc.perform(get("/users/getUserById/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUserByIdOk() throws Exception {
        User testUser = new User(1L,1L, "user");

        doReturn(testUser).when(service).getUserById(1L);


        mvc.perform(get("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.hashedCode").value("hashedCode"));
    }

    @Test
    public void testGetUserByTelegramIdWithNoContent() throws Exception {
        when(service.getUserByTelegramId(1L)).thenReturn(null);

        mvc.perform(get("/users/getUserByTelegramId/{telegramId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUserByTelegramIdOk() throws Exception {
        User testUser = new User(1L,1L, "user");

        when(service.getUserByTelegramId(1L)).thenReturn(testUser);

        mvc.perform(get("/users/getByTelegramId/{telegramId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.hashedCode").value("hashedCode"));
    }

    @Test
    public void testGetUserByNameWithNoContent() throws Exception {
        when(service.getUserByUsername("user")).thenReturn(null);

        mvc.perform(get("/users/getByName")
                        .param("username", "user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUserByNameOk() throws Exception {
        User testUser = new User(1L,1L, "user");

        when(service.getUserByUsername("user")).thenReturn(testUser);

        mvc.perform(get("/users/getByName")
                        .param("username", "user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.hashedCode").value("hashedCode"));
    }

    @Test
    public void testPostUserWithNoContent() throws Exception {
        User testUser = new User();

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostUserWithInvalidId() throws Exception {
        User testUser = new User(-28L, 1L, "user");

        when(repository.save(any(User.class))).thenReturn(testUser);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.id").value("Id must be a positive number"));
    }

    @Test
    public void testPostUserWithInvalidTelegramId() throws Exception {
        User testUser = new User(1L, -28L, "user");

        when(repository.save(any(User.class))).thenReturn(testUser);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.telegramId").value("TelegramId must be a positive number"));
    }

    @Test
    public void testPostUserWithInvalidName() throws Exception {
        User testUser = new User(1L, 1L, null);

        when(repository.save(any(User.class))).thenReturn(testUser);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("Username cannot be null"));
    }

    @Test
    public void testPostUserAlreadyExisted() throws Exception {
        User testUser = new User(1L, 1L, "user");

        doReturn(true).when(service).userExists(any(Long.class), any(Long.class));

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                        .andExpect(status().isConflict())
                        .andExpect(content().string("User already exists"));
    }

    @Test
    public void testPostUserOk() throws Exception {
        User testUser = new User(1L, 1L, "user");

        when(repository.save(any(User.class))).thenReturn(testUser);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                        .andExpect(status().isOk());
    }

}
