package vn.iotstar.service.impl;


import vn.iotstar.dao.UserDAO;
import vn.iotstar.dao.impl.UserDAOImpl;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;



public class UserServiceImpl 
        implements UserService {



    private UserDAO userDAO =
            new UserDAOImpl();




    @Override
    public User login(
            String username,
            String password
    ){


        User user = userDAO.findByUsername(username);
        if (user == null || !Boolean.TRUE.equals(user.getActive())) {
            return null;
        }
        String stored = user.getPassword();
        boolean valid = stored != null && stored.equals(password);
        return valid ? user : null;


    }




    @Override
    public User findByUsername(
            String username
    ){

        return userDAO.findByUsername(username);

    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }





    @Override
    public boolean register(
            User user
    ){


        try {


            if (userDAO.findByUsername(user.getUsername()) != null
                    || userDAO.findByEmail(user.getEmail()) != null) {
                return false;
            }
            userDAO.insert(user);


            return true;


        }catch(Exception e){

            e.printStackTrace();

            return false;

        }


    }


}
