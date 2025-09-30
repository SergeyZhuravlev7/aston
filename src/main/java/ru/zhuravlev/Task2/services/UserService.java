package ru.zhuravlev.Task2.services;


import ru.zhuravlev.Task2.daos.UserDAO;
import ru.zhuravlev.Task2.entitys.User;

import java.util.Collection;

public class UserService {

    private final UserDAO<User, Long> userDAO;

    public UserService(UserDAO<User, Long> userDAO) {
        this.userDAO = userDAO;
    }

    void saveUser(User user) {
        userDAO.save(user);
    }

    User readUser(long id) {
        return userDAO.getUserById(id);
    }

    Object readUser(String field,String value) {
        return switch (field) {
            case "Name" -> userDAO.getUsersByName(value);
            case "Email" -> userDAO.getUserByEmail(value);
            default -> null;
        };
    }

    void update(User user,long id) {
        userDAO.update(user,id);
    }

    void delete(long id) {
        userDAO.delete(id);
    }

    Collection<User> findAll() {
        return userDAO.findAll();
    }
}
