package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.User;


public interface UserService {
//    User getCreateUser(User user, Set<UserRole> userRoles) throws Exception;

    void save(User user);

    User findByUsername(String username);

    User findByName(String email);

//    ResponseMessage getCreateUser(ProductRequest userRequest);
//
//    ResponseMessage addNewUser(UserRequest userRequest);


}