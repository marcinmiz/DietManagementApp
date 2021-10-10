package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.DietaryPreferencesProduct;
import agh.edu.pl.diet.entities.DietaryPreferencesRecipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietaryPreferencesRecipesRepo extends CrudRepository<DietaryPreferencesRecipe, Long> {
}
