package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.Meals;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepo extends CrudRepository<Meals, Long> {
    Meals findByMealsName(String mealsName);
}
