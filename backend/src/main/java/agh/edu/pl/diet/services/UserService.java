package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.payloads.request.UserLoginRequest;
import agh.edu.pl.diet.payloads.request.UserRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import org.springframework.validation.BindingResult;


public interface UserService {

    User findByUsername(String username);

    ResponseMessage registerUser(UserRequest userRequest, BindingResult bindingResult);

    ResponseMessage loginUser(UserLoginRequest userLoginRequest);

    User getLoggedUser();

    }