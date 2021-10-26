package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.DailyMenu;
import agh.edu.pl.diet.entities.DietaryProgramme;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyMenuRepo extends CrudRepository<DailyMenu, Long> {
    DailyMenu findByDailyMenuName(String dailyMenuName);
    List<DailyMenu> findByDietaryProgramme(DietaryProgramme dietaryProgramme);
}
