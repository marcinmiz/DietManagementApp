package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.RecipeStep;
import org.springframework.data.repository.CrudRepository;

public interface RecipeStepsRepo extends CrudRepository<RecipeStep, Long> {
}
