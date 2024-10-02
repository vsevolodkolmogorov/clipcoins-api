package com.clipcoins.api.controller;

import com.clipcoins.api.model.User;
import com.clipcoins.api.repository.UserRepository;
import com.clipcoins.api.service.UserService;
import com.clipcoins.api.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTestMvc {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository repository;

    @MockBean
    private JwtUtil jwtUtil;

    @SpyBean
    private UserService service;

    @InjectMocks
    private UserController controller;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------------- GET --------------------

    @Test
    public void testGetAllUsersWithNoContent() throws Exception {
        when(service.getAllUsers()).thenReturn(Collections.emptyList());

        mvc.perform(get("/users/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.error").value("User list is empty"));
    }

    @Test
    public void testGetAllUsersOk() throws Exception {
        List<User> testUsers = List.of(
                new User(1L, 1L, "user1"),
                new User(2L, 2L, "user2"),
                new User(3L, 3L, "user3")
        );

        when(service.getAllUsers()).thenReturn(testUsers);

        mvc.perform(get("/users/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(testUsers.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    public void testGetUserByIdWithNoContent() throws Exception {
        int userId = 1;

        when(service.getUserById(1L)).thenReturn(null);

        mvc.perform(get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User with id " + userId + " not exist"));
    }

    @Test
    public void testGetUserByIdOk() throws Exception {
        User testUser = new User(1L, 1L, "user");

        doReturn(testUser).when(service).getUserById(1L);


        mvc.perform(get("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    public void testGetUserByTelegramIdWithNoContent() throws Exception {
        int telegramId = 1;

        when(service.getUserByTelegramId(telegramId)).thenReturn(null);

        mvc.perform(get("/users/getByTelegramId/{telegramId}", telegramId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User with telegramId " + telegramId + " not exist"));
    }

    @Test
    public void testGetUserByTelegramIdOk() throws Exception {
        User testUser = new User(1L, 1L, "user");

        when(service.getUserByTelegramId(1L)).thenReturn(testUser);

        mvc.perform(get("/users/getByTelegramId/{telegramId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    public void testGetUserByNameWithNoContent() throws Exception {
        String name = "user";

        when(service.getUserByUsername(name)).thenReturn(null);

        mvc.perform(get("/users/getByName")
                        .param("username", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User with name " + name + " not exist"));
    }

    @Test
    public void testGetUserByNameOk() throws Exception {
        User testUser = new User(1L, 1L, "user");

        when(service.getUserByUsername("user")).thenReturn(testUser);

        mvc.perform(get("/users/getByName")
                        .param("username", "user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("user"));
    }

    // ---------------- POST --------------------

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
    public void testPostUserWithNullName() throws Exception {
        User testUser = new User(1L, 1L, null);

        when(repository.save(any(User.class))).thenReturn(testUser);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("Username cannot be empty"));
    }

    @Test
    public void testPostUserAlreadyExisted() throws Exception {
        User testUser = new User(1L, 1L, "user");

        doReturn(testUser).when(service).getUserById(any(Long.class));

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("User already exists"));
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

    // ---------------- PUT --------------------

    @Test
    public void testPutUserWithNoContent() throws Exception {
        User testUser = new User();

        mvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User does not exist"));
    }

    @Test
    public void testPutUserWithInvalidId() throws Exception {
        User testUser = new User(-28L, 1L, "user");

        when(repository.save(any(User.class))).thenReturn(testUser);

        mvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User does not exist"));
    }

    @Test
    public void testPutUserWithNotExistedId() throws Exception {
        User testUser = new User(28L, 1L, "user");

        when(repository.save(any(User.class))).thenReturn(testUser);

        mvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User does not exist"));
    }

    @Test
    public void testPutUserWithInvalidTelegramId() throws Exception {
        User testUser = new User(1L, -28L, "user");

        when(repository.save(any(User.class))).thenReturn(testUser);

        mvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User does not exist"));
    }

    @Test
    public void testPutUserWithNullName() throws Exception {
        User oldUser = new User(1L, 1L, "user");
        oldUser.setRole("USER");

        User testUser = new User(1L, 1L, null);
        testUser.setRole("ADMIN");

        doReturn(oldUser).when(service).getUserById(any(Long.class));

        mvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.username").value("user"))
                        .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    public void testPutUserOk() throws Exception {
        User oldUser = new User(1L, 1L, "user");
        /*
        Null because we will not fill in the fields when sending data,
        so the user is sent with empty values, as it were
        */
        User testUser = new User(1L, 1L, "test");
        testUser.setRole(null);

        when(repository.save(any(User.class))).thenReturn(testUser);

        doReturn(oldUser).when(service).getUserById(any(Long.class));

        mvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                        .andExpect(status().isOk());
    }

    // ---------------- DELETE --------------------

    @Test
    public void testDeleteUserWithNoContent() throws Exception {
        long userId = Long.MAX_VALUE;

        mvc.perform(delete("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User does not exist"));
    }

    @Test
    public void testDeleteUserWithInvalidId() throws Exception {
        Long userId = -1L;

        mvc.perform(delete("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User does not exist"));
    }

    @Test
    public void testDeleteUserWithNonExistedId() throws Exception {
        Long userId = Long.MAX_VALUE;

        mvc.perform(delete("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User does not exist"));
    }

    @Test
    public void testDeleteUserOk() throws Exception {
        User testUser = new User(1L, 1L, "user");

        doReturn(testUser).when(service).getUserById(any(Long.class));

        mvc.perform(delete("/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
