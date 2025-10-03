package ru.zhuravlev.Task2.daos;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.zhuravlev.Task2.entitys.User;
import ru.zhuravlev.Task2.util.DAOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
class UserDAOImplTest {

    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

    private static SessionFactory sessionFactory;
    private static UserDAOImpl userDAO;
    static User user1;
    static User user2;
    static User user3;
    static User user4;
    static User user5;

    @BeforeAll
    static void beforeAll() {
        postgres.start();

        System.setProperty("hibernate.connection.url",postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username",postgres.getUsername());
        System.setProperty("hibernate.connection.password",postgres.getPassword());

        sessionFactory = new Configuration().addAnnotatedClass(ru.zhuravlev.Task2.entitys.User.class).buildSessionFactory();
        userDAO = new UserDAOImpl(sessionFactory);

        user1 = new User("test1", "test@test1", 30);
        user2 = new User("test2", "test@test2", 31);
        user3 = new User("test3", "test@test3", 32);
        user4 = new User("test4", "test@test4", 33);
        user5 = new User("test5", "test@test5", 34);
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
        User actualUser = user1;

        User expectedUser = userDAO.getUserById(1);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    @Order(3)
    void getUsersByNameShouldReturnListOfUser2() {
        User actualUser = user2;

        List<User> expectedUsers = userDAO.getUsersByName("test2");

        assertNotNull(expectedUsers);
        assertEquals(expectedUsers.getFirst(), actualUser);
        assertEquals(1,expectedUsers.size());
    }

    @Test
    @Order(4)
    void getUserByEmailShouldReturnUser3() {
        User actualUser = user3;

        User expectedUser = userDAO.getUserByEmail("test@test3");

        assertEquals(expectedUser, actualUser);
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
    void updateShouldNotThrow() {
        User user = new User();
        long id = 1;
        user.setName("NewTestName");
        user.setEmail("newTest@Email");
        user.setAge(80);

        assertDoesNotThrow(() -> userDAO.update(user,id));
    }

    @Test
    @Order(7)
    void deleteShouldDeleteUser() {
        long id = 1;

        assertDoesNotThrow(() -> userDAO.delete(id));
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
        User user = getErrorUser();
        long notExistingId = Long.MAX_VALUE;

        assertThrows(DAOException.class, () -> userDAO.update(user,notExistingId));
    }

    @Test
    void deleteUserShouldThrowException() {
        User user = getErrorUser();
        long notExistingId = Long.MAX_VALUE;

        assertThrows(DAOException.class, () -> userDAO.delete(notExistingId));
    }

    @Test
    void findByIdShouldReturnNull() {
        long notExistingId = Long.MAX_VALUE;

        User expectedUser = userDAO.getUserById(notExistingId);

        assertNull(expectedUser);
    }

    private User getErrorUser() {
        User user = new User();
        user.setName("ErrorName");
        user.setEmail("error@Email");
        user.setAge(80);
        return user;
    }
}