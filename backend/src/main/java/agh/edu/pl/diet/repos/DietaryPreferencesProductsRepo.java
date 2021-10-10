package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.DietaryPreferences;
import agh.edu.pl.diet.entities.DietaryPreferencesProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietaryPreferencesProductsRepo extends CrudRepository<DietaryPreferencesProduct, Long> {
}
