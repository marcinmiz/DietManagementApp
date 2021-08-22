package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends CrudRepository<Role, Long> {
//    Role findByName(String name);
}