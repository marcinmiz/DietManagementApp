package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.RecipeProduct;
import org.springframework.data.repository.CrudRepository;

public interface RecipeProductsRepo extends CrudRepository<RecipeProduct, Long> {
}
