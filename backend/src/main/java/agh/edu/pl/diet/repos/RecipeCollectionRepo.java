package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.RecipeCollection;
import agh.edu.pl.diet.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface RecipeCollectionRepo extends CrudRepository<RecipeCollection, Long> {
    RecipeCollection findByRecipeCollector(User recipeCollector);
}
