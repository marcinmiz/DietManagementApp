package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.DietType;

import java.util.List;

public interface DietTypeService {

    List<DietType> getAllDietTypes();

    Double calculateCalories(String diet_type, Double targetWeight);
    Double calculateProtein(String diet_type, Double targetWeight);
    Double calculateCarbohydrates(String diet_type, Double targetWeight);
    Double calculateFats(String diet_type, Double targetWeight);

    }
