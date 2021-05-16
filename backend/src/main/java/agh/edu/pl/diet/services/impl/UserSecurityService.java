package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (null == user) {
            throw new UsernameNotFoundException("Username not found");
        }

        return user;
    }

}