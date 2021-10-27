package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.DailyMenu;
import agh.edu.pl.diet.entities.DietaryProgramme;
import agh.edu.pl.diet.entities.Meals;
import agh.edu.pl.diet.entities.Recipes;
import agh.edu.pl.diet.payloads.request.DailyMenuRequest;
import agh.edu.pl.diet.payloads.response.DailyMenuResponse;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import org.springframework.data.util.Pair;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public interface DailyMenuService {

    DailyMenu getDailyMenuByProgrammeDay(Long dailyMenuId);

//    Meals getMeals(Long dailyMenuId);

//    DietaryProgramme getDietaryProgramme(Long dailyMenuId);

    ResponseMessage verifyRecipe(Recipes recipe, Map<String, List<Double>> dailyNutrientsScopes);

    ResponseMessage addNewDailyMenu(DietaryProgramme dietaryProgramme, Double totalDailyCalories, Integer mealsQuantity, Map<String, Double> totalDailyNutrients, Integer currentDay, Integer lastDay);

    List<DailyMenuResponse> getDietaryProgrammeDailyMenus(Long dietaryProgrammeId);

    ResponseMessage updateDailyMenu(Long dailyMenuId, DailyMenuRequest dailyMenuRequest);

    ResponseMessage removeDailyMenu(Long dailyMenuId);

}
