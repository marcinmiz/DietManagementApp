package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.*;
import agh.edu.pl.diet.repos.DailyMenuRepo;
import agh.edu.pl.diet.repos.MealRepo;
import agh.edu.pl.diet.services.ShoppingListService;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingListServiceImpl implements ShoppingListService {
    @Autowired
    private UserService userService;
    @Autowired
    private DailyMenuRepo dailyMenuRepo;
    @Autowired
    private MealRepo mealRepo;

    @Override
    public List<List<String>> getShoppingList() {

        List<Map<String, String>> shoppingLists = new ArrayList<>();

        User currentLoggedUser = userService.findByUsername(userService.getLoggedUser().getUsername());
        if (currentLoggedUser == null) {
            return null;
        }

        DietaryProgramme currentDietaryProgramme = currentLoggedUser.getCurrentDietaryProgramme();
        List<DailyMenu> menus = dailyMenuRepo.findByDietaryProgramme(currentDietaryProgramme);

        Integer counter = -1;

        for (int i = 0; i < menus.size(); i++) {

            if (i % 7 == 0) {
                shoppingLists.add(new LinkedHashMap<>());
                counter++;
            }

            List<Meals> meals = mealRepo.findByDailyMenu(menus.get(i));
            for (Meals meal : meals) {
                Recipes recipe = meal.getRecipe();
                List<RecipeProduct> ingredients = recipe.getRecipeProducts();
                for (RecipeProduct ingredient : ingredients) {
                    String unit = ingredient.getProductUnit();
                    Double amount = ingredient.getProductAmount();

                    switch (unit) {
                        case "g":
                            if (!ingredient.getProduct().getProductType().equalsIgnoreCase("by weight")) {
                                return null;
                            }
                            break;
                        case "kg":
                            if (!ingredient.getProduct().getProductType().equalsIgnoreCase("by weight")) {
                                return null;
                            }
                            amount *= 1000;
                            break;
                        case "dag":
                            if (!ingredient.getProduct().getProductType().equalsIgnoreCase("by weight")) {
                                return null;
                            }
                            amount *= 10;
                            break;
                        case "pcs":
                            if (!ingredient.getProduct().getProductType().equalsIgnoreCase("pieces")) {
                                return null;
                            }
                            amount = amount * ingredient.getProduct().getAverageWeight();
                            break;
                        case "ml":
                            if (!ingredient.getProduct().getProductType().equalsIgnoreCase("liquid")) {
                                return null;
                            }
                            break;
                        case "l":
                            if (!ingredient.getProduct().getProductType().equalsIgnoreCase("liquid")) {
                                return null;
                            }
                            amount *= 1000;
                            break;
                        case "tbsp":
                            if (!ingredient.getProduct().getProductType().equalsIgnoreCase("liquid")) {
                                return null;
                            }
                            amount *= 15;
                            break;
                        case "tsp":
                            if (!ingredient.getProduct().getProductType().equalsIgnoreCase("liquid")) {
                                return null;
                            }
                            amount *= 5;
                            break;
                        default:
                    }

                    if (!shoppingLists.get(counter).containsKey(ingredient.getProduct().getProductName())) {
                        String newUnit;
                        if (amount >= 1000.0) {
                            amount /= 1000;
                            newUnit = ingredient.getProduct().getProductType().equalsIgnoreCase("liquid") ? "l" : "kg";
                        } else {
                            newUnit = ingredient.getProduct().getProductType().equalsIgnoreCase("liquid") ? "ml" : "g";
                        }
                        shoppingLists.get(counter).put(ingredient.getProduct().getProductName(), amount + " " + newUnit);
                    } else {
                        String ingredientAmount = shoppingLists.get(counter).get(ingredient.getProduct().getProductName());
                        String[] parts = ingredientAmount.split(" ");
                        Double newAmount = Double.parseDouble(parts[0]);
                        String newUnit = parts[1];
                        if (newUnit.equalsIgnoreCase("kg") || newUnit.equalsIgnoreCase("l")) {
                            newAmount *= 1000;
                        }
                        newAmount += amount;
                        if (newAmount >= 1000.0) {
                            newAmount /= 1000;
                            newUnit = newUnit.equalsIgnoreCase("ml") ? "l" : "kg";
                        }
                        shoppingLists.get(counter).put(ingredient.getProduct().getProductName(), newAmount + " " + newUnit);
                    }
                }
            }
        }

        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < shoppingLists.size(); i++) {
            list.add(new ArrayList<>());
            for (String key : shoppingLists.get(i).keySet()) {
                list.get(i).add(key);
                list.get(i).add(shoppingLists.get(i).get(key));
            }
        }
        return list;
    }
}
