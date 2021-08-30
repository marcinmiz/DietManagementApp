package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.DietType;
import agh.edu.pl.diet.payloads.request.DietTypeRequest;
import agh.edu.pl.diet.services.DietTypeService;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class KetoDiet extends DietTypeRequest implements PropertyChangeListener, DietTypeService {

    @Override
    public List<DietType> getAllDietType() {
        return null;
    }

    @Override
    public double calculateCalories() {
        double calculatedCalories = calculateProtein() * 4 + calculateCarbohydrates() * 4 + calculateFats() * 9;
        return calculatedCalories;
    }

    @Override
    public double calculateProtein() {
        double calculatedProtein = targetWeight * 1;
        return calculatedProtein;
    }

    @Override
    public double calculateCarbohydrates() {
        double calculatedCarbohydrates = targetWeight * 0.5;
        return calculatedCarbohydrates;
    }

    @Override
    public double calculateFats() {
        double calculatedFats = targetWeight * 1.5;
        return calculatedFats;
    }


    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

        if (propertyChangeEvent.getNewValue().equals("calculateCalories")) {
            calculateCalories();
        } else if (propertyChangeEvent.getNewValue().equals("calculateProtein")) {
            calculateProtein();
        } else if (propertyChangeEvent.getNewValue().equals("calculateCarbohydrates")) {
            calculateCarbohydrates();
        } else if (propertyChangeEvent.getNewValue().equals("calculateFats")) {
            calculateFats();
        }
    }
}
