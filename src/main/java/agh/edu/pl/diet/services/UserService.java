package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.entities.security.UserRole;

import java.util.Set;


public interface UserService {
	User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	User save(User user);
}
