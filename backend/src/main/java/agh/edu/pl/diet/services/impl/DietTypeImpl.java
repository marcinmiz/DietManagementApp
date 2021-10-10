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
    public List<DietType> getAllDietTypes() {
        List<DietType> list = new ArrayList<>();
        dietTypeRepo.findAll().forEach(list::add);
        return list;
    }

    public Double calculateCalories(String diet_type, Double targetWeight) {
        return calculateProtein(diet_type, targetWeight) * 4 + calculateCarbohydrates(diet_type, targetWeight) * 4 + calculateFats(diet_type, targetWeight) * 9;
    }

    @Override
    public Double calculateProtein(String diet_type, Double targetWeight) {
        Double calculatedProtein = 0.0;
        switch (diet_type.toLowerCase()) {
            case "regular":
                //get coefficients from database
                calculatedProtein = targetWeight * 0.9;
                break;
            case "bodybuilding":
                calculatedProtein = targetWeight * 1.5;
                break;
            case "ketogenic":
                calculatedProtein = targetWeight * 1.0;
                break;
                default:

        }
        return calculatedProtein;
    }

    @Override
    public Double calculateCarbohydrates(String diet_type, Double targetWeight) {
        Double calculatedCarbohydrates = 0.0;
        switch (diet_type.toLowerCase()) {
            case "regular":
                calculatedCarbohydrates = targetWeight * 4.0;
                break;
            case "bodybuilding":
                calculatedCarbohydrates = targetWeight * 1.0;
                break;
            case "ketogenic":
                calculatedCarbohydrates = targetWeight * 0.5;
                break;
        }
        return calculatedCarbohydrates;
    }

    @Override
    public Double calculateFats(String diet_type, Double targetWeight) {
        Double calculatedFats = 0.0;
        switch (diet_type.toLowerCase()) {
            case "regular":
                calculatedFats = targetWeight * 0.5;
                break;
            case "bodybuilding":
                calculatedFats = targetWeight * 0.5;
                break;
            case "ketogenic":
                calculatedFats = targetWeight * 1.5;
                break;
        }
        return calculatedFats;
    }
}
