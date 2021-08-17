package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.payloads.request.UserLoginRequest;
import agh.edu.pl.diet.payloads.request.UserRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.payloads.validators.UserValidator;
import agh.edu.pl.diet.repos.RoleRepo;
import agh.edu.pl.diet.repos.UserRepo;
import agh.edu.pl.diet.services.SecurityService;
import agh.edu.pl.diet.services.UserService;
import agh.edu.pl.diet.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityService securityService;

    private BCryptPasswordEncoder encoder = SecurityUtility.passwordEncoder();

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public ResponseMessage registerUser(UserRequest userRequest, BindingResult bindingResult) {
        userValidator.validate(userRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            return new ResponseMessage(message);
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setPassword(encoder.encode(userRequest.getPassword()));
        String creationDate = new Date().toInstant().toString();
        user.setCreationDate(creationDate);
        user.setRole(roleRepo.findByName("USER"));

        userRepo.save(user);

        return new ResponseMessage("User has been registered");
    }

    @Override
    public ResponseMessage loginUser(UserLoginRequest userLoginRequest) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (authentication.isAuthenticated()) {

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new ResponseMessage("User has been logged in");

        }

        return new ResponseMessage("User has not been logged in. Check username and password");
    }

    @Override
    public User getLoggedUser() {
        String username = securityService.findLoggedInUsername();
        if (username != null) {
            User user = userRepo.findByUsername(username);
            if (user != null) {
                return user;
            }
            return null;
        }
        return null;
    }

}