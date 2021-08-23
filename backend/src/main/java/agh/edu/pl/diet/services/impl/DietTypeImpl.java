package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.DietType;
import agh.edu.pl.diet.repos.DietTypeRepo;
import agh.edu.pl.diet.services.DietTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DietTypeImpl implements DietTypeService {

    @Autowired
    DietTypeRepo dietTypeRepo;

    @Override
    public List<DietType> getAllDietType() {
        List<DietType> list = new ArrayList<>();
        dietTypeRepo.findAll().forEach(list::add);
        return list;
    }
}
