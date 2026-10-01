package vn.iotstar.dao.impl;

import jakarta.persistence.EntityManager;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.UserDAO;
import vn.iotstar.entity.User;

public class UserDAOImpl implements UserDAO {
    @Override
    public User findByUsername(String username){
        EntityManager em =
                JpaConfig.getEntityManager();
        try {
            return em.find(
                    User.class,
                    username
            );
        }finally {em.close();}

    }
    @Override
    public User findByEmail(String email) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)", User.class)
                    .setParameter("email", email)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }
    @Override
    public void insert(User user){
        EntityManager em =
                JpaConfig.getEntityManager();
        try {
            em.getTransaction()
                    .begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
