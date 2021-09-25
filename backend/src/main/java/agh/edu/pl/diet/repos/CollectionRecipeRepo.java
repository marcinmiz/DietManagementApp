package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.CollectionRecipe;
import agh.edu.pl.diet.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CollectionRecipeRepo extends CrudRepository<CollectionRecipe, Long> {
    List<CollectionRecipe> findByRecipeCollector(User recipeCollector);
}
