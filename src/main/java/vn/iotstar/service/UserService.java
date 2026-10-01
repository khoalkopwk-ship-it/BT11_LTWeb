package vn.iotstar.service;


import vn.iotstar.entity.User;


public interface UserService {


    User login(
            String username,
            String password
    );


    User findByUsername(
            String username
    );

    User findByEmail(String email);


    boolean register(
            User user
    );


}
