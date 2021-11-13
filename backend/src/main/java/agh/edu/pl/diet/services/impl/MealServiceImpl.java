package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.DailyMenu;
import agh.edu.pl.diet.entities.Meals;
import agh.edu.pl.diet.entities.Recipes;
import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.DailyMenuRepo;
import agh.edu.pl.diet.repos.MealRepo;
import agh.edu.pl.diet.services.MealService;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepo mealRepo;

    @Autowired
    private DailyMenuRepo menuRepo;

    @Autowired
    private UserService userService;

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

    @Override
    public ResponseMessage markMealAsConsumed(Long mealId, String mark) {

        Meals meal = mealRepo.findById(mealId).orElse(null);

        if (meal == null) {
            return new ResponseMessage("Meal with id " + mealId + " has not been found");
        }

        switch (mark) {
            case "mark":
                meal.setConsumed(true);
                break;
            case "unmark":
                meal.setConsumed(false);
                break;
            default:
                return new ResponseMessage("Wrong mark: " + mark);
        }

        mealRepo.save(meal);

        return new ResponseMessage("Meal " + meal.getMealsName() + " has been " + mark + "ed as consumed");
    }

    @Override
    public List<Double> getCaloriesConsumed() {

        List<Double> consumed = new ArrayList<>();
        Double consumedCalories = 0.0, totalCalories = 0.0, part = 0.0;

        User loggedUser = userService.findByUsername(userService.getLoggedUser().getUsername());
        if (loggedUser == null) {
            return null;
        }

        if (loggedUser.getCurrentDietaryProgramme() == null) {
            return new ArrayList<>();
        }

        List<DailyMenu> menus = menuRepo.findByDietaryProgramme(loggedUser.getCurrentDietaryProgramme());

        Integer today = Calendar.getInstance().toInstant().atZone(ZoneId.systemDefault()).getDayOfWeek().getValue();

        System.out.println(today);

        menus = menus.stream().filter(menu -> Instant.parse(menu.getDailyMenuDate()).atZone(ZoneId.systemDefault()).getDayOfWeek().getValue() == today).collect(Collectors.toList());

        for (DailyMenu menu: menus) {
            List<Meals> meals = mealRepo.findByDailyMenu(menu);

            for (Meals meal: meals) {
                Recipes recipe = meal.getRecipe();
                Double calories = recipe.getRecipeCalories();

                if (meal.getConsumed()) {
                    consumedCalories += calories;
                }

                totalCalories += calories;
            }
        }

        Long tempConsumedCalories = Math.round(consumedCalories * 100);

        Long tempTotalCalories = Math.round(totalCalories * 100);

        part = (consumedCalories/totalCalories) * 100;
        Long tempPart = Math.round(part * 100);
        part = Double.valueOf(tempPart) / 100;

        consumed.add(Double.valueOf(tempConsumedCalories) / 100);
        consumed.add(Double.valueOf(tempTotalCalories) / 100);
        consumed.add(part);
        consumed.add(100.0 - part);

        return consumed;
    }
}
