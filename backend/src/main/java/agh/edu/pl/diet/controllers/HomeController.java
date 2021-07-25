package agh.edu.pl.diet.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String index() {
        return "redirect:/now";
    }

    @RequestMapping("/now")
    public String home() {  
        return "now";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}