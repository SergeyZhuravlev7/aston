package ru.zhuravlev.Task2.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zhuravlev.Task2.daos.UserDAOImpl;
import ru.zhuravlev.Task2.entitys.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserDAOImpl userDAO;

    @InjectMocks
    UserService userService;

    User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void saveUserShouldCallDAO() {
        doNothing().when(userDAO).save(user);

        userService.saveUser(user);

        verify(userDAO).save(user);
    }

    @Test
    void readUserByIdShouldReturnUser() {
        when(userDAO.getUserById(1)).thenReturn(user);

        User expectedUser = userService.readUser(1);

        assertEquals(expectedUser, user);
    }

    @Test
    void readUserByIdShouldReturnNull() {
        when(userDAO.getUserById(1)).thenReturn(null);

        User expectedUser = userService.readUser(1);

        assertNull(expectedUser);
    }

    @Test
    void readUserByNameShouldReturnUser() {
        String field = "Name";
        String value = "SomeName";
        when(userDAO.getUsersByName(value)).thenReturn(List.of(user));

        List<User> expectedUsers = (List<User>) userService.readUser(field,value);

        assertEquals(1,expectedUsers.size());
        assertEquals(expectedUsers.getFirst(), user);
    }

    @Test
    void readUserByNameShouldReturnNull() {
        String field = "Name";
        String value = "SomeName";
        when(userDAO.getUsersByName(value)).thenReturn(null);

        List<User> expectedUsers = (List<User>) userService.readUser(field,value);

        assertNull(expectedUsers);
    }

    @Test
    void readUserByEmailShouldReturnUser() {
        String field = "Email";
        String value = "SomeEmail";
        when(userDAO.getUserByEmail(value)).thenReturn(user);

        User expectedUser = (User) userService.readUser(field,value);

        assertEquals(expectedUser, user);
    }

    @Test
    void readUserByEmailShouldReturnNull() {
        String field = "Email";
        String value = "SomeEmail";
        when(userDAO.getUserByEmail(value)).thenReturn(null);

        User expectedUser = (User) userService.readUser(field,value);

        assertNull(expectedUser);
    }

    @Test
    void updateShouldCallDAO() {
        long id = 1;
        doNothing().when(userDAO).update(user, id);

        userService.update(user, id);

        verify(userDAO).update(user, id);
    }

    @Test
    void deleteShouldCallDAO() {
        long id = 1;
        doNothing().when(userDAO).delete(id);

        userService.delete(id);

        verify(userDAO).delete(id);
    }

    @Test
    void findAllShouldReturnListOfUsers() {
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        List<User> users = Arrays.asList(user1,user2,user3);
        for(User user : users) {
            user.setName("TestName");
        }
        when(userDAO.findAll()).thenReturn(users);

        List<User> expectedUsers = (List<User>) userService.findAll();

        assertEquals(expectedUsers.size(), users.size());
        for(User user : expectedUsers) {
            assertEquals(user.getName(), "TestName");
        }
    }

    @Test
    void findAllShouldReturnEmptyList() {
        when(userDAO.findAll()).thenReturn(new ArrayList<>());

        List<User> expectedUsers = (List<User>) userService.findAll();

        assertNotNull(expectedUsers);
        assertEquals(0,expectedUsers.size());
    }
}