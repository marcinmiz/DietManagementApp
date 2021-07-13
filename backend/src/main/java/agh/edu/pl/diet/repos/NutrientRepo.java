package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.Nutrient;
import org.springframework.data.repository.CrudRepository;

public interface NutrientRepo extends CrudRepository<Nutrient, Long> {
    Nutrient findByNutrientName(String nutrientName);
}
