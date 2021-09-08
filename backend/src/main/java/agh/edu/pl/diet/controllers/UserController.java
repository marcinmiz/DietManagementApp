package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.payloads.request.UserLoginRequest;
import agh.edu.pl.diet.payloads.request.UserRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:3000"})
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody UserRequest userRequest, BindingResult bindingResult) {

        String message = userService.registerUser(userRequest, bindingResult).getMessage();

        if (!message.equals("User " + userRequest.getName() + " " + userRequest.getSurname() + " has been registered")) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseMessage(message));
        }

        return ResponseEntity.ok(new ResponseMessage(message));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> loginUser(@AuthenticationPrincipal @RequestBody UserLoginRequest userLoginRequest) {

        String message = userService.loginUser(userLoginRequest).getMessage();

        if (!message.equals("User has been logged in")) {

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));

        }

        return ResponseEntity.ok(new ResponseMessage(message));

    }

    @GetMapping("/logout")
    public ResponseEntity<ResponseMessage> logoutUser() {
        return ResponseEntity.ok(new ResponseMessage("User has been logged out"));
    }

    @GetMapping("/loggedUser")
    public ResponseEntity<User> getLoggedUser() {

        User user = userService.getLoggedUser();

        return ResponseEntity.ok(user);
    }

    @GetMapping("/existsUser/{username}")
    public ResponseEntity<Boolean> existsUser(@PathVariable String username) {

        Boolean user = userService.existsUser(username);

        return ResponseEntity.ok(user);
    }

}