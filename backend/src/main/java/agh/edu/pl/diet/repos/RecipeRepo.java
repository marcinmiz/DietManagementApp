package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.Recipes;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepo extends CrudRepository<Recipes, Long> {
    Recipes findByRecipeName(String recipeName);

}
