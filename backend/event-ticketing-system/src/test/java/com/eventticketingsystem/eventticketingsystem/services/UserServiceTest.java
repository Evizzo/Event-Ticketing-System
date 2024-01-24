package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.auth.AuthenticationService;
import com.eventticketingsystem.eventticketingsystem.entities.User;
import com.eventticketingsystem.eventticketingsystem.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testFindUserById() {
        User user = new User();
        user.setId(UUID.fromString("a4a8456f-23ce-4f02-9b69-13bbdd74a045"));
        when(userRepository.findById(UUID.fromString("a4a8456f-23ce-4f02-9b69-13bbdd74a045"))).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findUserById(UUID.fromString("a4a8456f-23ce-4f02-9b69-13bbdd74a045"));

        assertEquals(user, foundUser.orElse(null));
    }
    @Test
    public void testSaveUser() {
        User user = new User();
        user.setId(UUID.fromString("a4a8456f-23ce-4f02-9b69-13bbdd74a045"));
        user.setFirstname("testFirstname");
        user.setLastname("testLastname");
        user.setPassword("password");
        user.setCredits(BigDecimal.valueOf(1000));
        user.setEmail("testUser@gmail.com");

        userService.saveUser(user);

        verify(userRepository, times(1)).save(user);
        assertEquals(UUID.fromString("a4a8456f-23ce-4f02-9b69-13bbdd74a045"), user.getId());
        assertEquals("testFirstname", user.getFirstname());
        assertEquals("testLastname",user.getLastname());
        assertNotNull("Password", user.getPassword());
        assertEquals(BigDecimal.valueOf(1000), user.getCredits());
        assertEquals("testUser@gmail.com", user.getEmail());
    }
    @Test
    public void testFindAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        when(userRepository.findAll()).thenReturn(userList);

        List<User> foundUsers = userService.findAllUsers();

        assertEquals(userList, foundUsers);
    }
}