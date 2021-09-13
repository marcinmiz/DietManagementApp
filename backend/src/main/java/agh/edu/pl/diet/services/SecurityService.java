package agh.edu.pl.diet.services;

public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String username, String password);

}
