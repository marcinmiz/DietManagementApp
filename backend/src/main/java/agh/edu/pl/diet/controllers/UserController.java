package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.entities.Weight;
import agh.edu.pl.diet.payloads.request.*;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/changePassword")
    public ResponseEntity<ResponseMessage> resetPassword(@RequestBody ChangePasswordRequest request, BindingResult bindingResult) {
        ResponseMessage message = userService.changePassword(request, bindingResult);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<ResponseMessage> resetEmail(@RequestBody ForgotPasswordRequest request, BindingResult bindingResult) {
        ResponseMessage message = userService.changeEmail(request, bindingResult);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/addWeight/{weight}")
    public ResponseEntity<ResponseMessage> addWeight(@PathVariable Double weight) {
        ResponseMessage message = userService.addWeight(weight);
        if (message.getMessage().equals("Weight " + weight + " has been added")) {
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
    }

    @GetMapping("/getLoggedUserWeights")
    public ResponseEntity<List<List<Weight>>> getLoggedUserWeights() {
        List<List<Weight>> weights = userService.getLoggedUserWeights();
        if (weights == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
        return ResponseEntity.ok(weights);
    }

    @GetMapping("/getWeightTrend")
    public ResponseEntity<Double> getWeightTrend() {
        Double weightTrend = userService.getWeightTrend();
        if (weightTrend == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
        return ResponseEntity.ok(weightTrend);
    }
}