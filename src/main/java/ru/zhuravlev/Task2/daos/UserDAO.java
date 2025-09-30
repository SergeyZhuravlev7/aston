package ru.zhuravlev.Task2.daos;

import java.util.Collection;

public interface UserDAO<User,Long> {

    void save(User user);

    User getUserById(long id);

    Collection<User> getUsersByName(String name);

    User getUserByEmail(String email);

    void update(User user,long id);

    void delete(long id);

    Collection<User> findAll();
}
