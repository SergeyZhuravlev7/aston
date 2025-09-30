package ru.zhuravlev.Task2.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfig {

    static public SessionFactory getSessionFactory() {
        return new Configuration().addAnnotatedClass(ru.zhuravlev.Task2.entitys.User.class).buildSessionFactory();
    }


}
