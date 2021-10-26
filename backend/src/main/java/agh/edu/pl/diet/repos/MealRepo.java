package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.DailyMenu;
import agh.edu.pl.diet.entities.Meals;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepo extends CrudRepository<Meals, Long> {
    List<Meals> findByDailyMenu(DailyMenu dailyMenu);
}
