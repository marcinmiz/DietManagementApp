package agh.edu.pl.diet.services;

public interface SecurityService {
    String findLoggedInUsername();

    void autologin(String username, String password);
}
