package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.DietaryPreferences;
import org.springframework.data.repository.CrudRepository;

public interface DietaryPreferencesRepo extends CrudRepository<DietaryPreferences, Long> {
    DietaryPreferences findByDietaryPreferencesName(String DietaryPreferencesName);
}
