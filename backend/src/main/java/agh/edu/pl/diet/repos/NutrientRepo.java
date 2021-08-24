package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.Nutrient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutrientRepo extends CrudRepository<Nutrient, Long> {
    Nutrient findByNutrientName(String nutrientName);
}
