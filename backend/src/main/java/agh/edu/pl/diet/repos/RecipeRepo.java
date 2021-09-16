package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.Recipes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepo extends CrudRepository<Recipes, Long> {
    Recipes findByRecipeId(Long recipeId);
    Recipes findByRecipeName(String recipeName);

}
