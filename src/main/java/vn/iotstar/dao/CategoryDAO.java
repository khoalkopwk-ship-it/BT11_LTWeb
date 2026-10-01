package vn.iotstar.dao;

import vn.iotstar.entity.Category;

import java.util.List;

public interface CategoryDAO {
    List<Category> findAll();

    List<Category> findAllActive();

    Category findById(int id);
}
