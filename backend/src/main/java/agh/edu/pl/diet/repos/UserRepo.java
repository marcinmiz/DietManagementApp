package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface UserRepo extends CrudRepository<User, Long> {
    User findByUsername(String username);

    //User findByName(String email);
    User findByGoogleUsername(String googleUsername);

    User findByGoogleName(String googleName);
}