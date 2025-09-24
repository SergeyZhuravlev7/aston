package ru.zhuravlev.Task2.daos;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Collection;

public interface UserDAO<User,Long> {

    SessionFactory sessionFactory =
            new Configuration().addAnnotatedClass(ru.zhuravlev.Task2.entitys.User.class).buildSessionFactory();

    void save(User user);

    User getUserById(long id);

    Collection<User> getUsersByName(String name);

    User getUserByEmail(String email);

    void update(User user,long id);

    void delete(long id);

    Collection<User> findAll();
}
