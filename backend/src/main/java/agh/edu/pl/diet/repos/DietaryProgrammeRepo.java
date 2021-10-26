package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.DietaryProgramme;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietaryProgrammeRepo extends CrudRepository<DietaryProgramme, Long> {
    DietaryProgramme findByDietaryProgrammeName(String dietaryProgrammeName);
    List<DietaryProgramme> findByOwnerUserId(Long ownerId);
}
