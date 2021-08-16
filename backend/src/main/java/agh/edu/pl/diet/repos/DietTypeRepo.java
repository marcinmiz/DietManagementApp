package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.DietType;
import org.springframework.data.repository.CrudRepository;

public interface DietTypeRepo extends CrudRepository<DietType, Long> {
    DietType findByDietTypeName(String dietTypeName);
}
