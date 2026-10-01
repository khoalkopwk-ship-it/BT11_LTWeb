package vn.iotstar.dao.impl;

import jakarta.persistence.EntityManager;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.CategoryDAO;
import vn.iotstar.entity.Category;

import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {
    @Override
    public List<Category> findAll() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery("select c from Category c order by c.categoryname", Category.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Category> findAllActive() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery(
                            "select c from Category c where c.status = true order by c.categoryname", Category.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Category findById(int id) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }
}
