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

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepo mealRepo;

    @Override
    public ResponseMessage addNewMeal(String mealName, Recipes recipe, DailyMenu dailyMenu) {
        Meals meal = new Meals();
        Calendar mealHour = Calendar.getInstance();

        switch (mealName) {
            case "breakfast":
                mealHour.set(Calendar.HOUR_OF_DAY, 8);
                break;
            case "lunch":
                mealHour.set(Calendar.HOUR_OF_DAY, 11);
                break;
            case "dinner":
                mealHour.set(Calendar.HOUR_OF_DAY, 14);
                break;
            case "tea":
                mealHour.set(Calendar.HOUR_OF_DAY, 17);
                break;
            case "supper":
                mealHour.set(Calendar.HOUR_OF_DAY, 20);
                break;
            default:
                return new ResponseMessage("Wrong meal name");
        }

        mealHour.set(Calendar.MINUTE, 0);
        mealHour.set(Calendar.SECOND, 0);
        meal.setMealHourTime(mealHour.toInstant().toString());

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
