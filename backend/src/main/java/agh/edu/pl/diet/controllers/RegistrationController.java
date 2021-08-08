package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.payloads.request.UserValidator;
import agh.edu.pl.diet.services.SecurityService;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
//@RequestMapping("/api/")
public class RegistrationController {
//    @Autowired
//    private UserRepo userRepo;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirmation());

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {
        return "welcome";
    }

//    @GetMapping("/registration/{id}")
//    public User getRegistration(@PathVariable("id") Long user_id) {
//        return userService.save();
//    }

//    @GetMapping("/registration")
//    public String registration() {
//        return "registration";
//    }

//    @PostMapping("/registration/add")
//    public ResponseEntity<ResponseMessage> addNewUser(@RequestBody UserRequest userRequest) {
//        ResponseMessage message = userService.addNewUser(userRequest);
//        if (message.getMessage().endsWith(" has been added successfully")) {
//            return ResponseEntity.status(HttpStatus.OK).body(message);
//        } else {
//            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
//        }
//    }

//    @PostMapping("/registration")
//    public String addUser(String name, String surname, String username, String password, String passwordConfirmation, String email) {
//        User user = new User();
//        user.setName(name);
//        user.setSurname(surname);
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setPasswordConfirmation(passwordConfirmation);
//        user.setEmail(email);
//       // user.setEnabled(true);
//        //user.setRoles(Collections.singleton(Role.USER));
//
//        userRepo.save(user);
//
//        return "redirect:/login";
//    }
//
//    @PostMapping("/login")
//    public String loginUser(String username, String password, String email) {
//        User user = new User();
//        user.setUsername(username);
//        user.setEmail(email);
//        user.setPassword(passwordEncoder.encode(password));
//        // user.setEnabled(true);
//        //user.setRoles(Collections.singleton(Role.USER));
//
//        userRepo.save(user);
//
//        return "redirect:/now";
//    }
}