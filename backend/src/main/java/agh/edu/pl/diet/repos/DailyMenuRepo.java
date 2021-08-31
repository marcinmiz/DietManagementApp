package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.DailyMenu;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyMenuRepo extends CrudRepository<DailyMenu, Long> {
    DailyMenu findByDailyMenuName(String dailyMenuName);
}
