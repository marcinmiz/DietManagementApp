package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.security.Role;
import org.springframework.data.repository.CrudRepository;


public interface RoleRepo extends CrudRepository<Role, Long> {
	Role findByname(String name);
}
