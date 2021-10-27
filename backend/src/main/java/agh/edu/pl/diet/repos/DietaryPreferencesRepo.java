package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.DietaryPreferences;
import agh.edu.pl.diet.entities.DietaryProgramme;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietaryPreferencesRepo extends CrudRepository<DietaryPreferences, Long> {
    DietaryPreferences findByDietaryPreferenceId(Long dietaryPreferenceId);
    DietaryPreferences findByRelatedDietaryProgramme(DietaryProgramme dietaryProgramme);
}
