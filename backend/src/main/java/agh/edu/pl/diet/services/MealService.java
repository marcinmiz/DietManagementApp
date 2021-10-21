package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.DailyMenu;
import agh.edu.pl.diet.entities.Recipes;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import org.springframework.stereotype.Service;

@Service
public interface MealService {

    ResponseMessage addNewMeal(String mealName, Recipes recipe, DailyMenu dailyMenu);
}
