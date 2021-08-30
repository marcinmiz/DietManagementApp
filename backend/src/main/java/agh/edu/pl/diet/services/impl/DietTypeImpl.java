package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.DietType;
import agh.edu.pl.diet.payloads.request.DietTypeRequest;
import agh.edu.pl.diet.repos.DietTypeRepo;
import agh.edu.pl.diet.services.DietTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DietTypeImpl extends DietTypeRequest implements DietTypeService {

    @Autowired
    DietTypeRepo dietTypeRepo;

    @Override
    public List<DietType> getAllDietType() {
        List<DietType> list = new ArrayList<>();
        dietTypeRepo.findAll().forEach(list::add);
        return list;
    }

    public double calculateCalories() {
        double calculatedCalories = calculateProtein() * 4 + calculateCarbohydrates() * 4 + calculateFats() * 9;
        return calculatedCalories;
    }

    @Override
    public double calculateProtein() {
        double calculatedProtein = targetWeight * 0.9;
        return calculatedProtein;
    }

    @Override
    public double calculateCarbohydrates() {
        double calculatedCarbohydrates = targetWeight * 4;
        return calculatedCarbohydrates;
    }

    @Override
    public double calculateFats() {
        double calculatedFats = targetWeight * 0.5;
        return calculatedFats;
    }

//    public void setTargetWeight(double newTargetWeight) throws InvalidInputException {
//        if (newTargetWeight < 0) {
//            throw new InvalidInputException();
//        }
//        targetWeight = newTargetWeight; //
//    }
//
//    //Updates user's target macronutrients when notifyObservers() called
//    @Override
//    public void update(PropertyChangeListener pcl, Object arg) {
//        DietaryPreferences updatedDietaryPreferences = (DietaryPreferences)pcl;
//        targetWeight = updatedDietaryPreferences.getTargetWeight();
//        calories = updatedDietaryPreferences.getDietType().calculateCalories();
//        protein = updatedDietaryPreferences.getDietType().calculateProtein();
//        carbohydrates = updatedDietaryPreferences.getDietType().calculateCarbohydrates();
//        fats = updatedDietaryPreferences.getDietType().calculateFats();
//        System.out.println("I have updated");
//    }
}
