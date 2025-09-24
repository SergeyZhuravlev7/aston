package ru.zhuravlev.Task2.daos;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import ru.zhuravlev.Task2.entitys.User;
import ru.zhuravlev.Task2.util.DAOException;

import java.util.Collection;
import java.util.List;

public class UserDAOImpl implements UserDAO<User, Long> {


    @Override
    public void save(User user) {
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } catch (Exception ex) {
            rollback(session);
            throw new DAOException(ex.getMessage(),ex);
        }
    }

    @Override
    public User getUserById(long id) {
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            User user = session.get(User.class,id);
            session.getTransaction().commit();
            return user;
        } catch (Exception ex) {
            rollback(session);
            throw new DAOException(ex.getMessage(),ex);
        }
    }

    @Override
    public List<User> getUsersByName(String name) {
        String query = "SELECT u FROM User u WHERE u.name = :name";
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            List<User> users = session.createQuery(query,User.class)
                    .setParameter("name",name)
                    .getResultList();
            session.getTransaction().commit();
            if (! users.isEmpty()) return users;
            return null;
        } catch (Exception ex) {
            rollback(session);
            throw new DAOException(ex.getMessage(),ex);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.select(root).where(cb.equal(root.get("email"),email));
            User user = session.createQuery(criteriaQuery).getSingleResult();
            session.getTransaction().commit();
            return user;
        } catch (Exception ex) {
            rollback(session);
            throw new DAOException(ex.getMessage(),ex);
        }
    }

    @Override
    public void update(User newUser,long id) {
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            User oldUser = session.get(User.class,id);
            if (oldUser == null) throw new DAOException("User with id " + id + " does not exist");
            oldUser.setAge(newUser.getAge());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setName(newUser.getName());
            session.merge(oldUser);
            session.getTransaction().commit();
        } catch (Exception ex) {
            rollback(session);
            throw new DAOException(ex.getMessage(),ex);
        }
    }

    @Override
    public void delete(long id) {
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            User oldUser = session.get(User.class,id);
            session.remove(oldUser);
            session.getTransaction().commit();
        } catch (Exception ex) {
            rollback(session);
            throw new DAOException(ex.getMessage(),ex);
        }
    }

    @Override
    public Collection<User> findAll() {
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.select(root);
            List<User> userList = session.createQuery(criteriaQuery).getResultList();
            session.getTransaction().commit();
            return userList;
        }
        catch (Exception ex) {
            rollback(session);
            throw new DAOException(ex.getMessage(),ex);
        }
    }

    private void rollback(Session session) {
        if (session != null && session.getTransaction().isActive())
            session.getTransaction().rollback();
    }
}
