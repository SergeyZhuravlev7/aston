package ru.zhuravlev.Task2.daos;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.zhuravlev.Task2.entitys.User;
import ru.zhuravlev.Task2.util.DAOException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
class UserDAOImplTest {

    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

    private static SessionFactory sessionFactory;
    private static UserDAOImpl userDAO;

    @BeforeAll
    static void beforeAll() {
        postgres.start();

        System.setProperty("hibernate.connection.url",postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username",postgres.getUsername());
        System.setProperty("hibernate.connection.password",postgres.getPassword());

        sessionFactory = new Configuration().addAnnotatedClass(ru.zhuravlev.Task2.entitys.User.class).buildSessionFactory();
        userDAO = new UserDAOImpl(sessionFactory);

        User user1 = new User("test1", "test@test1", 30);
        User user2 = new User("test2", "test@test2", 31);
        User user3 = new User("test3", "test@test3", 32);
        User user4 = new User("test4", "test@test4", 33);
        User user5 = new User("test5", "test@test5", 34);
        for (User user : List.of(user1, user2, user3, user4, user5)) {
            userDAO.save(user);
        }
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    @Order(1)
    void saveShouldNotThrow() {
        User user = new User();
        user.setName("NewEntity");
        user.setEmail("test@testEntity");
        user.setAge(80);

        assertDoesNotThrow(() -> userDAO.save(user));
    }

    @Test
    @Order(2)
    void getUserByIdShouldReturnUser1() {
        User user = new User();
        user.setId(1);
        user.setName("test1");
        user.setEmail("test@test1");
        user.setAge(30);

        User expectedUser = userDAO.getUserById(1);

        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getName(), user.getName());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getAge(), user.getAge());
    }

    @Test
    @Order(3)
    void getUsersByNameShouldReturnListOfUser2() {
        User user = new User();
        user.setId(2);
        user.setName("test2");
        user.setEmail("test@test2");
        user.setAge(31);

        List<User> expectedUsers = userDAO.getUsersByName("test2");

        assertNotNull(expectedUsers);
        assertEquals(expectedUsers.getFirst().getName(), user.getName());
        assertEquals(expectedUsers.getFirst().getEmail(), user.getEmail());
        assertEquals(expectedUsers.getFirst().getAge(), user.getAge());
        assertEquals(1,expectedUsers.size());
    }

    @Test
    @Order(4)
    void getUserByEmailShouldReturnUser3() {
        User user = new User();
        user.setId(3);
        user.setName("test3");
        user.setEmail("test@test3");
        user.setAge(32);

        User expectedUser = userDAO.getUserByEmail("test@test3");

        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getName(), user.getName());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getAge(), user.getAge());
    }

    @Test
    @Order(5)
    void findAllShouldReturnListOf6Users() {
        List<User> expectedUsers = userDAO.findAll().stream().toList();

        assertNotNull(expectedUsers);
        assertEquals(6,expectedUsers.size());
    }

    @Test
    @Order(6)
    void updateShouldUpdateUser() {
        User user = new User();
        long id = 1;
        user.setName("NewTestName");
        user.setEmail("newTest@Email");
        user.setAge(80);

        userDAO.update(user,id);
        User expectedUser = userDAO.getUserById(id);

        assertEquals(expectedUser.getName(), user.getName());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getAge(), user.getAge());
    }

    @Test
    @Order(7)
    void deleteShouldDeleteUser() {
        long id = 1;

        userDAO.delete(id);

        User expectedUser = userDAO.getUserById(id);

        assertNull(expectedUser);
    }

    @Test
    @Order(8)
    void findDeletedUserByIdShouldReturnNull() {
        long id = 1;

        User expectedUser = userDAO.getUserById(id);

        assertNull(expectedUser);
    }

    @Test
    @Order(9)
    void findAllShouldReturnListOf5Users() {
        List<User> expectedUsers = userDAO.findAll().stream().toList();

        assertNotNull(expectedUsers);
        assertEquals(5,expectedUsers.size());
    }

    @Test
    void updateUserShouldThrowException() {
        User user = new User();
        user.setName("ErrorName");
        user.setEmail("error@Email");
        user.setAge(80);
        long notExistingId = Long.MAX_VALUE;

        assertThrows(DAOException.class, () -> userDAO.update(user,notExistingId));
    }

    @Test
    void deleteUserShouldThrowException() {
        User user = new User();
        user.setName("ErrorName");
        user.setEmail("error@Email");
        user.setAge(80);
        long notExistingId = Long.MAX_VALUE;

        assertThrows(DAOException.class, () -> userDAO.delete(notExistingId));
    }

    @Test
    void findByIdShouldReturnNull() {
        long notExistingId = Long.MAX_VALUE;
        User expectedUser = userDAO.getUserById(notExistingId);
        assertNull(expectedUser);
    }
}