package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.DailyMenu;
import agh.edu.pl.diet.entities.Meals;
import agh.edu.pl.diet.entities.Recipes;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.MealRepo;
import agh.edu.pl.diet.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepo mealRepo;

    @Override
    public ResponseMessage addNewMeal(String mealName, Recipes recipe, DailyMenu dailyMenu) {

        List<String> mealNames = List.of("breakfast", "lunch", "dinner", "tea", "supper");

        Meals meal = new Meals();

        if (!mealNames.contains(mealName)) {
            return new ResponseMessage("Wrong meal name");
        }

        meal.setMealsName(mealName);

        if (recipe == null) {
            return new ResponseMessage("Recipe is required");
        }

        meal.setRecipe(recipe);

        if (dailyMenu == null) {
            return new ResponseMessage("DailyMenu is required");
        }

        meal.setDailyMenu(dailyMenu);

        System.out.println(meal);

        mealRepo.save(meal);

        return new ResponseMessage("Meal has been added");
    }
}
