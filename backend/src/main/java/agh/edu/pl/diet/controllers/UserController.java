package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.payloads.request.UserLoginRequest;
import agh.edu.pl.diet.payloads.request.UserRequest;
import agh.edu.pl.diet.payloads.validators.UserValidator;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.RoleRepo;
import agh.edu.pl.diet.services.SecurityService;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody UserRequest userRequest, BindingResult bindingResult) {

        String message = userService.registerUser(userRequest, bindingResult).getMessage();

        if (!message.equals("User has been registered")) {
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseMessage(message));
        }

        return ResponseEntity.ok(new ResponseMessage(message));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest) {

        String message = userService.loginUser(userLoginRequest).getMessage();

        if (!message.equals("User has been logged in")) {

            return ResponseEntity.badRequest().body(new ResponseMessage(message));

        }

        return ResponseEntity.ok(new ResponseMessage(message));

    }

    @GetMapping("/loggedUser")
    public ResponseEntity<User> getLoggedUser() {

        User user = userService.getLoggedUser();

        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(user);
    }

}