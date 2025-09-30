package ru.zhuravlev.Task2.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zhuravlev.Task2.daos.UserDAOImpl;
import ru.zhuravlev.Task2.entitys.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserDAOImpl userDAO;

    @InjectMocks
    UserService userService;

    @Test
    void saveUserShouldCallDAO() {
        User user = new User();

        userService.saveUser(user);

        verify(userDAO).save(user);
    }

    @Test
    void readUserByIdShouldReturnUser() {
        User user = new User();
        when(userDAO.getUserById(1)).thenReturn(user);

        User expectedUser = userService.readUser(1);

        assertEquals(expectedUser, user);
    }

    @Test
    void readUserByIdShouldReturnNull() {
        User user = new User();
        when(userDAO.getUserById(1)).thenReturn(null);

        User expectedUser = userService.readUser(1);

        assertNull(expectedUser);
    }

    @Test
    void readUserByNameShouldReturnUser() {
        User user = new User();
        String field = "Name";
        String value = "SomeName";
        when(userDAO.getUsersByName(value)).thenReturn(List.of(user));

        List<User> expectedUsers = (List<User>) userService.readUser(field,value);

        assertEquals(expectedUsers.size(), 1);
        assertEquals(expectedUsers.get(0), user);
    }

    @Test
    void readUserByNameShouldReturnNull() {
        User user = new User();
        String field = "Name";
        String value = "SomeName";
        when(userDAO.getUsersByName(value)).thenReturn(null);

        List<User> expectedUsers = (List<User>) userService.readUser(field,value);

        assertNull(expectedUsers);
    }

    @Test
    void readUserByEmailShouldReturnUser() {
        User user = new User();
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
        User user = new User();

        userService.update(user, id);

        verify(userDAO).update(user, id);
    }

    @Test
    void deleteShouldCallDAO() {
        long id = 1;

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
}