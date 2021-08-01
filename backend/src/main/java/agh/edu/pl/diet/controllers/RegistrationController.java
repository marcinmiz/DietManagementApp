package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.payloads.request.ProductRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.UserRepo;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getRegistration(@PathVariable("id") Long user_id) {
        return userService.save();
    }

//    @GetMapping("/registration")
//    public String registration() {
//        return "registration";
//    }

    @PostMapping("/add")
    public ResponseMessage addNewUser(@RequestBody ProductRequest userRequest) {
        return userService.getCreateUser(userRequest);
    }

    @PostMapping("/registration")
    public String addUser(String name, String surname, String username, String password, String passwordConfirmation, String email) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setPasswordConfirmation(passwordConfirmation);
        user.setEmail(email);
        user.setEnabled(true);
        //user.setRoles(Collections.singleton(Role.USER));

        userRepo.save(user);

        return "redirect:/login";
    }
}