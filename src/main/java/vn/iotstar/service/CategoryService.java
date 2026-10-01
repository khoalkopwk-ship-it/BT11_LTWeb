package vn.iotstar.service;

import vn.iotstar.dao.CategoryDAO;
import vn.iotstar.dao.impl.CategoryDAOImpl;
import vn.iotstar.entity.Category;

import java.util.List;

public class CategoryService {
    private final CategoryDAO dao = new CategoryDAOImpl();

    public List<Category> findAll() {
        return dao.findAll();
    }

    public List<Category> findAllActive() {
        return dao.findAllActive();
    }

    public Category findById(int id) {
        return dao.findById(id);
    }
}
