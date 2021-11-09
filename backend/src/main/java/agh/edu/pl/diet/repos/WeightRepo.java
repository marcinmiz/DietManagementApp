package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.Category;
import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.entities.Weight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeightRepo extends CrudRepository<Weight, Long> {
    List<Weight> findByMeasurer(User measurer);
}
