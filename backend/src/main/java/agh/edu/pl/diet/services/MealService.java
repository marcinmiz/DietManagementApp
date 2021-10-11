package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.Meals;
import agh.edu.pl.diet.payloads.request.MealRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;

import java.util.List;

public interface MealService {

    List<Meals> getAllMeals();

    Meals getMeal(Long meal_id);

    ResponseMessage addNewMeal(MealRequest mealRequest);

    ResponseMessage updateMeals(Long mealId, MealRequest mealRequest);

    ResponseMessage removeMeal(Long mealId);

}