package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.payloads.request.*;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @PostMapping("/forgotPassword")
    public ResponseEntity<ResponseMessage> processForgotPassword(@RequestBody ForgotPasswordRequest request) {
        ResponseMessage message = userService.processForgotPassword(request.getEmail());
        return ResponseEntity.ok(message);
    }

    @PostMapping("/checkTokenValidity")
    public ResponseEntity<ResponseMessage> checkTokenValidity(@RequestBody CheckTokenValidityRequest request) {
        ResponseMessage message = userService.checkTokenValidity(request.getToken());
        return ResponseEntity.ok(message);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ResponseMessage> resetPassword(@RequestBody ResetPasswordRequest request, BindingResult bindingResult) {
        ResponseMessage message = userService.resetPassword(request, bindingResult);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/resetEmail")
    public ResponseEntity<ResponseMessage> resetEmail(@RequestBody ForgotPasswordRequest request, BindingResult bindingResult) {
        ResponseMessage message = userService.resetEmail(request, bindingResult);
        return ResponseEntity.ok(message);
    }
}