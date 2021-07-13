package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepo extends CrudRepository<Category, Long> {
    Category findByCategoryName(String categoryName);
}
