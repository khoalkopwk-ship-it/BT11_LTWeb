package vn.iotstar.dao;


import vn.iotstar.entity.User;


public interface UserDAO {


    User findByUsername(String username);

    User findByEmail(String email);


    void insert(User user);


}
