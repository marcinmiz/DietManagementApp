package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.RecipeCustomerSatisfaction;
import agh.edu.pl.diet.entities.Recipes;
import agh.edu.pl.diet.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface RecipeCustomerSatisfactionsRepo extends CrudRepository<RecipeCustomerSatisfaction, Long> {
    RecipeCustomerSatisfaction findByCustomerSatisfactionOwnerAndRecipe(User customerSatisfactionOwner, Recipes recipe);

}
