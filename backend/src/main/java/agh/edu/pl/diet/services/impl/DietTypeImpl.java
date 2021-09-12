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
        double calculatedProtein = targetWeight * Double.parseDouble(String.valueOf(multiplicationCalculateProtein));
        return calculatedProtein;
    }

    @Override
    public double calculateCarbohydrates() {
        double calculatedCarbohydrates = targetWeight * Double.parseDouble(String.valueOf(multiplicationCalculateCarbohydrates));
        return calculatedCarbohydrates;
    }

    @Override
    public double calculateFats() {
        double calculatedFats = targetWeight * Double.parseDouble(String.valueOf(multiplicationCalculateFats));
        return calculatedFats;
    }

    private final Type multiplicationCalculateProtein;
    private final Type multiplicationCalculateCarbohydrates;
    private final Type multiplicationCalculateFats;

    public DietTypeImpl(Type multiplicationCalculateProtein, Type multiplicationCalculateCarbohydrates, Type multiplicationCalculateFats) {
        this.multiplicationCalculateProtein = multiplicationCalculateProtein;
        this.multiplicationCalculateCarbohydrates = multiplicationCalculateCarbohydrates;
        this.multiplicationCalculateFats = multiplicationCalculateFats;
    }

}
