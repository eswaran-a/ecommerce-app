package com.ecommerce.controllers;

import com.ecommerce.TestUtils;
import com.ecommerce.controller.UserController;
import com.ecommerce.entity.User;
import com.ecommerce.exception.InvalidUserDetailsException;
import com.ecommerce.exception.UserExistsException;
import com.ecommerce.exception.UserNotFoundException;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.request.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void initialise() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUser() {
        when(encoder.encode("password1")).thenReturn("hashedpassword");
        CreateUserRequest userRequest = new CreateUserRequest("user1",
                "password1",
                "password1");
        final ResponseEntity<User> newUserResponse = userController.createUser(userRequest);
        assertNotNull(newUserResponse);
        assertEquals(200, newUserResponse.getStatusCodeValue());
        User newUser = newUserResponse.getBody();
        assertNotNull(newUser);
        assertEquals(0, newUser.getId());
        assertEquals("user1", newUser.getUsername());
        assertEquals("hashedpassword", newUser.getPassword());
    }

    @Test
    public void createUser_userAlreadyExists() {
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(new User()));
        CreateUserRequest userRequest = new CreateUserRequest("user1",
                "password1",
                "password1");

        assertThrows(UserExistsException.class, () -> {
            userController.createUser(userRequest);
        });
    }

    @Test
    public void createUser_invalidPasswordFieldLength() {
        CreateUserRequest userRequest = new CreateUserRequest("user1",
                "passwo",
                "passwo");

        assertThrows(InvalidUserDetailsException.class, () -> {
            userController.createUser(userRequest);
        });
    }

    @Test
    public void createUser_passwordMismatch() {
        CreateUserRequest userRequest = new CreateUserRequest("user1",
                "password2",
                "password1");

        assertThrows(InvalidUserDetailsException.class, () -> {
            userController.createUser(userRequest);
        });
    }

    @Test
    public void findUserById(){
        when(userRepository.findById(1l)).thenReturn(Optional.of(new User(1l, "user1")));

        final ResponseEntity<User> userResponse = userController.findById(1l);
        assertNotNull(userResponse);
        assertEquals(200, userResponse.getStatusCodeValue());
        User newUser = userResponse.getBody();
        assertNotNull(newUser);
        assertEquals(1, newUser.getId());
        assertEquals("user1", newUser.getUsername());
    }

    @Test
    public void findUserByName(){
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(new User(1l, "user1")));


        final ResponseEntity<User> userResponse = userController.findByUserName("user1");
        assertNotNull(userResponse);
        assertEquals(200, userResponse.getStatusCodeValue());
        User newUser = userResponse.getBody();
        assertNotNull(newUser);
        assertEquals(1, newUser.getId());
        assertEquals("user1", newUser.getUsername());
    }

    @Test
    public void userNotFound() {
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            userController.findByUserName("user1");
        });
    }
}
