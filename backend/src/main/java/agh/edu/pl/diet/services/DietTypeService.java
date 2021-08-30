package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.DietType;

import java.util.List;

public interface DietTypeService {

    List<DietType> getAllDietType();

 //   public void update(Object o);

    public double calculateCalories();
    public double calculateProtein();
    public double calculateCarbohydrates();
    public double calculateFats();

    }
