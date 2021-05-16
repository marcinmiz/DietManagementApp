package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.entities.security.UserRole;
import agh.edu.pl.diet.repos.RoleRepo;
import agh.edu.pl.diet.repos.UserRepo;
import agh.edu.pl.diet.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public User createUser(User user, Set<UserRole> userRoles) {
        User localUser = userRepo.findByUsername(user.getUsername());

        if (localUser != null) {
            LOG.info("user {} already exists. Nothing will be done.", user.getUsername());
        } else {
            for (UserRole ur : userRoles) {
                roleRepo.save(ur.getRole());
            }

            user.getUserRoles().addAll(userRoles);

            localUser = userRepo.save(user);
        }

        return localUser;
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

}