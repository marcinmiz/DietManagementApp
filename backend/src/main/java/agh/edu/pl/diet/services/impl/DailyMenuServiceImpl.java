package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.*;
import agh.edu.pl.diet.payloads.request.DailyMenuRequest;
import agh.edu.pl.diet.payloads.request.RecipeGetRequest;
import agh.edu.pl.diet.payloads.response.DailyMenuResponse;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.DailyMenuRepo;
import agh.edu.pl.diet.repos.DietaryPreferencesRepo;
import agh.edu.pl.diet.repos.DietaryProgrammeRepo;
import agh.edu.pl.diet.repos.MealRepo;
import agh.edu.pl.diet.services.DailyMenuService;
import agh.edu.pl.diet.services.MealService;
import agh.edu.pl.diet.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DailyMenuServiceImpl implements DailyMenuService {

    @Autowired
    private DailyMenuRepo dailyMenuRepo;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private MealService mealService;

    @Autowired
    private MealRepo mealRepo;

    @Autowired
    private DietaryProgrammeRepo dietaryProgrammeRepo;

    @Autowired
    private DietaryPreferencesRepo preferenceRepo;

//    @Autowired
//    private NutrientRepo nutrientRepo;
//    @Autowired
//    private CategoryRepo categoryRepo;
//    @Autowired
//    private UserRepo userRepo;

    private ResponseMessage verify(String mode, String type, Object item) {
        switch (type) {
            case "name":
                String name = String.valueOf(item);
                if (name == null) {
                    return new ResponseMessage("Daily Menu name has to be given");
                } else if (name.length() < 2 || name.length() > 40) {
                    return new ResponseMessage("Daily Menu name has to have min 2 and max 40 characters");
                } else if (!(name.matches("^[a-zA-Z ]+$"))) {
                    return new ResponseMessage("Daily Menu name has to contain only letters and spaces");
                } else if (!mode.equals("update") && dailyMenuRepo.findByDailyMenuName(name) != null) {
                    return new ResponseMessage("Daily Menu with this name exists yet");
                } else {
                    return new ResponseMessage("Daily Menu name is valid");
                }
//            case "calories":
//                if (item == null) {
//                    return new ResponseMessage("Product calories has to be given");
//                }
//
//                Integer calories = Integer.parseInt(item.toString());
//
//                if (calories.toString().length() < 1 || calories.toString().length() > 10) {
//                    return new ResponseMessage("Product calories has to have min 1 and max 10 characters");
//                } else if (!(calories.toString().matches("^0$") || calories.toString().matches("^(-)?[1-9]\\d*$"))) {
//                    return new ResponseMessage("Product calories has to contain only digits");
//                } else if (calories < 0) {
//                    return new ResponseMessage("Product calories has to be greater or equal 0");
//                } else {
//                    return new ResponseMessage("Product calories are valid");
//                }
//            case "category":
//                String categoryName = String.valueOf(item);
//                if (categoryName == null) {
//                    return new ResponseMessage("Product category has to be given");
//                } else if (categoryName.equals("")) {
//                    return new ResponseMessage("Product category has to be chosen");
//                } else if (categoryRepo.findByCategoryName(categoryName) == null) {
//                    return new ResponseMessage("Product category does not exist");
//                } else {
//                    return new ResponseMessage("Product category is valid");
//                }
//            case "list":
//                List<String> nutrients = (List<String>) item;
//                if (nutrients == null) {
//                    return new ResponseMessage("Product nutrients has to be given");
//                } else if (nutrients.isEmpty()) {
//                    return new ResponseMessage("At least 1 product nutrient is required");
//                } else {
//                    return new ResponseMessage("Product nutrients are valid");
//                }
//            case "nutrientStatement":
//                String nutrientStatement = String.valueOf(item);
//                if (nutrientStatement.equals("")) {
//                    return new ResponseMessage("Product nutrient has to be defined");
//                } else if (!(nutrientStatement.matches("^[a-zA-Z]+;0$") || nutrientStatement.matches("^[a-zA-Z]+;(-)?[1-9]\\d*$"))) {
//                    return new ResponseMessage("Product nutrient has to match format \"productName;productAmount\"");
//                } else {
//                    return new ResponseMessage("Product nutrient statement is valid");
//                }
//            case "nutrientName":
//                String nutrientName = String.valueOf(item);
//
//                if (nutrientRepo.findByNutrientName(nutrientName) == null) {
//                    return new ResponseMessage("Product nutrient name has to be proper");
//                } else {
//                    return new ResponseMessage("Product nutrient name is valid");
//                }
//            case "nutrientAmount":
//                Double nutrientAmount = Double.valueOf(item.toString());
//
//                if (nutrientAmount.toString().length() < 1 || nutrientAmount.toString().length() > 20) {
//                    return new ResponseMessage("Product nutrient amount has to have min 1 and max 20 characters");
//                } else if (nutrientAmount < 0) {
//                    return new ResponseMessage("Product nutrient amount has to be greater or equal 0");
//                } else {
//                    return new ResponseMessage("Product nutrient amount is valid");
//                }
//
        }

        return new ResponseMessage("Invalid type");
    }

    @Override
    public DailyMenu getDailyMenuByProgrammeDay(Long dailyMenuId) {
        DailyMenu dailyMenu = dailyMenuRepo.findById(dailyMenuId).get();
        return dailyMenu;
    }

//    @Override
//    public Meals getMeals(Long dailyMenuId) {
//        Meals meals = mealRepo.findById(dailyMenuId).get();
//        return meals;
//    }
//
//    @Override
//    public DietaryProgramme getDietaryProgramme(Long dailyMenuId) {
//        DietaryProgramme dietaryProgramme = dietaryProgrammeRepo.findById(dailyMenuId).get();
//        return dietaryProgramme;
//    }

    @Override
    public ResponseMessage verifyRecipe(Recipes recipe, Map<String, List<Double>> dailyNutrientsScopes) {

        Double ultimateForce = 1.0;

        Set<String> keySet = dailyNutrientsScopes.keySet();

        String[] nutrientNames
                = keySet.toArray(new String[keySet.size()]);

        for (int i = 0; i < nutrientNames.length; i++) {

            String nutrientName = nutrientNames[i];

            if (recipe.getRecipeNutrients(nutrientName) < dailyNutrientsScopes.get(nutrientName).get(0) || recipe.getRecipeNutrients(nutrientName) > dailyNutrientsScopes.get(nutrientName).get(1)) {
                return new ResponseMessage("Recipe has inappropriate amount of " + nutrientName);
            }

        }

        RecipeGetRequest request = new RecipeGetRequest();
        request.setRecipesGroup("personal");
        request.setPhrase("");
        List<Recipes> collectionRecipes = recipeService.getRecipes(request);

        if (collectionRecipes.stream().anyMatch(collectionRecipe -> collectionRecipe.getRecipeId().equals(recipe.getRecipeId())))
            ultimateForce *= 0.7;
        else
            ultimateForce *= 0.3;

        Map<String, Integer> ingredientIncidence = new LinkedHashMap<>();

        List<RecipeProduct> recipeProducts = recipe.getRecipeProducts();

        for (RecipeProduct recipeProduct : recipeProducts) {
            String categoryName = recipeProduct.getProduct().getCategory().getCategoryName();
            if (ingredientIncidence.containsKey(categoryName)) {
                Integer newValue = ingredientIncidence.get(categoryName) + 1;
                ingredientIncidence.put(categoryName, newValue);
            } else {
                ingredientIncidence.put(categoryName, 0);
            }
        }

        String majorCategoryProductName = Collections.max(ingredientIncidence.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        switch (majorCategoryProductName) {
            case "Fruit":
            case "Vegetables":
                ultimateForce *= 0.75;
                break;
            case "Cereal":
                ultimateForce *= 0.7;
                break;
            case "Dairy":
                ultimateForce *= 0.5;
                break;
            case "Meat":
            case "Fish":
                ultimateForce *= 0.25;
                break;
            case "Fats":
            case "Sweets":
            case "Nuts":
                ultimateForce *= 0.15;
                break;
            default:
                ultimateForce *= 1.0;
        }

        List<RecipeCustomerSatisfaction> recipeRatings = recipe.getRecipeCustomerSatisfactions().stream().filter(satisfaction -> satisfaction.getRecipeRating() != null).collect(Collectors.toList());

        if (!recipeRatings.isEmpty()) {
            OptionalDouble average = recipeRatings.stream().mapToDouble(RecipeCustomerSatisfaction::getRecipeRating).average();
            if (average.isPresent()) {
                Long averageTempRating = Math.round(average.getAsDouble() * 1000);

                Double averageRating = Double.valueOf(averageTempRating) / 1000;

                if (averageRating < 3.0) {
                    ultimateForce *= 0.0;
                }

            }
        }

        if (ultimateForce < 0.15) {
            return new ResponseMessage("Recipe is inappropriate in regard to dietary preference");
        }

        return new ResponseMessage("Recipe is appropriate in regard to dietary preference");
    }

    @Override
    public ResponseMessage addNewDailyMenu(DietaryProgramme dietaryProgramme, Double totalDailyCalories, Integer mealsQuantity, Map<String, Double> totalDailyNutrients, Integer currentDay, Integer lastDay) {
        DailyMenu dailyMenu = new DailyMenu();

        if (dietaryProgramme == null) {
            return new ResponseMessage("DietaryProgramme is required");
        } else if (totalDailyCalories <= 0) {
            return new ResponseMessage("totalDailyCalories has to be greater than 0");
        } else if (!(mealsQuantity.equals(4) || mealsQuantity.equals(5))) {
            return new ResponseMessage("Meals quantity has to be equal to 4 or 5");
        } else if (totalDailyNutrients == null) {
            return new ResponseMessage("totalDailyNutrients is required");
        } else if (!totalDailyNutrients.keySet().equals(Set.of("Protein", "Carbohydrate", "Fat"))) {
            return new ResponseMessage("totalDailyNutrients has to contain Protein, Carbohydrate and Fat");
        }
//        else if (startDate == null) {
//            return new ResponseMessage("startDate is required");
//        } else if (startDate.compareTo(Calendar.getInstance()) >= 0) {
//            return new ResponseMessage("startDate cannot be past");
//        }
        if (currentDay == null) {
            return new ResponseMessage("currentDay is required");
        } else if (currentDay <= 0) {
            return new ResponseMessage("currentDay has to be greater than 0");
        } else if (lastDay == null) {
            return new ResponseMessage("lastDay is required");
        } else if (lastDay <= 0) {
            return new ResponseMessage("lastDay has to be greater than 0");
        }

        RecipeGetRequest request = new RecipeGetRequest();
        request.setRecipesGroup("accepted");
        request.setPhrase("");
        List<Recipes> recipes = recipeService.getRecipes(request);

        Map<String, List<Double>> nutrientsScopes = new LinkedHashMap<>();
        nutrientsScopes.put("breakfast", List.of(0.25, 0.3));
        nutrientsScopes.put("lunch", List.of(0.05, 0.1));
        nutrientsScopes.put("dinner", List.of(0.35, 0.4));
        if (mealsQuantity.equals(5)) {
            nutrientsScopes.put("tea", List.of(0.05, 0.1));
            nutrientsScopes.put("supper", List.of(0.15, 0.2));
        } else {
            nutrientsScopes.put("supper", List.of(0.25, 0.3));
        }

        List<Recipes> chosenRecipes = new ArrayList<>();
        List<Recipes> potentialRecipes;

        Set<String> keySet = nutrientsScopes.keySet();

        String[] mealNames
                = keySet.toArray(new String[keySet.size()]);

        Double caloriesDifference = 0.0;
        Double proteinsDifference = 0.0;
        Double carbohydratesDifference = 0.0;
        Double fatsDifference = 0.0;
        Double leftCalories = totalDailyCalories;
        Double leftProteins = totalDailyNutrients.get("Protein");
        Double leftCarbohydrates = totalDailyNutrients.get("Carbohydrate");
        Double leftFats = totalDailyNutrients.get("Fat");
        Double lowerCaloriesLimit, upperCaloriesLimit, lowerProteinsLimit, upperProteinsLimit, lowerCarbohydratesLimit, upperCarbohydratesLimit, lowerFatsLimit, upperFatsLimit;
        Map<String, List<Double>> dailyNutrientsScopes = new LinkedHashMap<>();

        for (int i = 0; i < mealsQuantity; i++) {
            String mealName = mealNames[i];

            Long caloriesDiff = Math.round(caloriesDifference * 100);
            Double caloriesDiff2 = Double.valueOf(caloriesDiff) / 100;

            if (caloriesDiff2.equals(0.0)) {
                lowerCaloriesLimit = (nutrientsScopes.get(mealName).get(0)) * totalDailyCalories;
                upperCaloriesLimit = (nutrientsScopes.get(mealName).get(1)) * totalDailyCalories;
            } else if (caloriesDiff2 > 0.0 || i == mealsQuantity - 1) {
                lowerCaloriesLimit = (nutrientsScopes.get(mealName).get(0) + 0.025) * totalDailyCalories + caloriesDifference;
                upperCaloriesLimit = (nutrientsScopes.get(mealName).get(1) + 0.025) * totalDailyCalories + caloriesDifference;
            } else {
                lowerCaloriesLimit = (nutrientsScopes.get(mealName).get(0)) * totalDailyCalories + caloriesDifference;
                upperCaloriesLimit = (nutrientsScopes.get(mealName).get(1)) * totalDailyCalories + caloriesDifference;
            }

            Long proteinsDiff = Math.round(proteinsDifference * 100);
            Double proteinsDiff2 = Double.valueOf(proteinsDiff) / 100;

            if (proteinsDiff2.equals(0.0)) {
                lowerProteinsLimit = (nutrientsScopes.get(mealName).get(0)) * totalDailyNutrients.get("Protein");
                upperProteinsLimit = (nutrientsScopes.get(mealName).get(1)) * totalDailyNutrients.get("Protein");
            } else if (proteinsDiff2 > 0.0 || i == mealsQuantity - 1) {
                lowerProteinsLimit = (nutrientsScopes.get(mealName).get(0) + 0.025) * totalDailyNutrients.get("Protein") + proteinsDifference;
                upperProteinsLimit = (nutrientsScopes.get(mealName).get(1) + 0.025) * totalDailyNutrients.get("Protein") + proteinsDifference;
            } else {
                lowerProteinsLimit = (nutrientsScopes.get(mealName).get(0)) * totalDailyNutrients.get("Protein") + proteinsDifference;
                upperProteinsLimit = (nutrientsScopes.get(mealName).get(1)) * totalDailyNutrients.get("Protein") + proteinsDifference;
            }
            dailyNutrientsScopes.put("Protein", List.of(lowerProteinsLimit, upperProteinsLimit));

            Long carbohydratesDiff = Math.round(carbohydratesDifference * 100);
            Double carbohydratesDiff2 = Double.valueOf(carbohydratesDiff) / 100;

            if (carbohydratesDiff2.equals(0.0)) {
                lowerCarbohydratesLimit = (nutrientsScopes.get(mealName).get(0)) * totalDailyNutrients.get("Carbohydrate");
                upperCarbohydratesLimit = (nutrientsScopes.get(mealName).get(1)) * totalDailyNutrients.get("Carbohydrate");
            } else if (carbohydratesDiff2 > 0.0 || i == mealsQuantity - 1) {
                lowerCarbohydratesLimit = (nutrientsScopes.get(mealName).get(0) + 0.025) * totalDailyNutrients.get("Carbohydrate") + carbohydratesDifference;
                upperCarbohydratesLimit = (nutrientsScopes.get(mealName).get(1) + 0.025) * totalDailyNutrients.get("Carbohydrate") + carbohydratesDifference;
            } else {
                lowerCarbohydratesLimit = (nutrientsScopes.get(mealName).get(0)) * totalDailyNutrients.get("Carbohydrate") + carbohydratesDifference;
                upperCarbohydratesLimit = (nutrientsScopes.get(mealName).get(1)) * totalDailyNutrients.get("Carbohydrate") + carbohydratesDifference;
            }
            dailyNutrientsScopes.put("Carbohydrate", List.of(lowerCarbohydratesLimit, upperCarbohydratesLimit));

            Long fatsDiff = Math.round(fatsDifference * 100);
            Double fatsDiff2 = Double.valueOf(fatsDiff) / 100;

            if (fatsDiff2.equals(0.0)) {
                lowerFatsLimit = (nutrientsScopes.get(mealName).get(0)) * totalDailyNutrients.get("Fat");
                upperFatsLimit = (nutrientsScopes.get(mealName).get(1)) * totalDailyNutrients.get("Fat");
            } else if (fatsDiff2 > 0.0 || i == mealsQuantity - 1) {
                lowerFatsLimit = (nutrientsScopes.get(mealName).get(0) + 0.025) * totalDailyNutrients.get("Fat") + fatsDifference;
                upperFatsLimit = (nutrientsScopes.get(mealName).get(1) + 0.025) * totalDailyNutrients.get("Fat") + fatsDifference;
            } else {
                lowerFatsLimit = (nutrientsScopes.get(mealName).get(0)) * totalDailyNutrients.get("Fat") + fatsDifference;
                upperFatsLimit = (nutrientsScopes.get(mealName).get(1)) * totalDailyNutrients.get("Fat") + fatsDifference;
            }
            dailyNutrientsScopes.put("Fat", List.of(lowerFatsLimit, upperFatsLimit));

            System.out.println("lowerCaloriesLimit " + lowerCaloriesLimit);
            System.out.println("upperCaloriesLimit " + upperCaloriesLimit);

            {
                Double finalLowerCaloriesLimit = lowerCaloriesLimit;
                Double finalUpperCaloriesLimit = upperCaloriesLimit;

                potentialRecipes = recipes.stream().filter(recipe -> recipe.getRecipeCalories() >= finalLowerCaloriesLimit && recipe.getRecipeCalories() <= finalUpperCaloriesLimit).collect(Collectors.toList());
                System.out.println("id");
                potentialRecipes.stream().map(Recipes::getRecipeId).forEach(System.out::println);
            }

            for (Recipes verifiedRecipe : potentialRecipes) {

                if (verifyRecipe(verifiedRecipe, dailyNutrientsScopes).getMessage().equals("Recipe is appropriate in regard to dietary preference") && !chosenRecipes.contains(verifiedRecipe)) {
                    System.out.println(verifiedRecipe.getRecipeName());
                    chosenRecipes.add(verifiedRecipe);

                    Double verifiedRecipeTotalCalories = verifiedRecipe.getRecipeCalories();
                    Double averageMealTotalCalories = (nutrientsScopes.get(mealName).get(0) + 0.025) * totalDailyCalories;

                    if (verifiedRecipeTotalCalories < averageMealTotalCalories) {
                        caloriesDifference += averageMealTotalCalories - verifiedRecipeTotalCalories;
                    } else {
                        caloriesDifference -= verifiedRecipeTotalCalories - averageMealTotalCalories;
                    }

                    Double verifiedRecipeTotalProteins = verifiedRecipe.getRecipeNutrients("Protein");
                    Double averageMealTotalProteins = (nutrientsScopes.get(mealName).get(0) + 0.025) * totalDailyNutrients.get("Protein");

                    if (verifiedRecipeTotalProteins < averageMealTotalProteins) {
                        proteinsDifference += averageMealTotalProteins - verifiedRecipeTotalProteins;
                    } else {
                        proteinsDifference -= verifiedRecipeTotalProteins - averageMealTotalProteins;
                    }

                    Double verifiedRecipeTotalCarbohydrates = verifiedRecipe.getRecipeNutrients("Carbohydrate");
                    Double averageMealTotalCarbohydrates = (nutrientsScopes.get(mealName).get(0) + 0.025) * totalDailyNutrients.get("Carbohydrate");

                    if (verifiedRecipeTotalCarbohydrates < averageMealTotalCarbohydrates) {
                        carbohydratesDifference += averageMealTotalCarbohydrates - verifiedRecipeTotalCarbohydrates;
                    } else {
                        carbohydratesDifference -= verifiedRecipeTotalCarbohydrates - averageMealTotalCarbohydrates;
                    }

                    leftCalories = leftCalories - verifiedRecipeTotalCalories;
                    leftProteins = leftProteins - verifiedRecipe.getRecipeNutrients("Protein");
                    leftCarbohydrates = leftCarbohydrates - verifiedRecipe.getRecipeNutrients("Carbohydrate");
                    leftFats = leftFats - verifiedRecipe.getRecipeNutrients("Fat");

                    System.out.println("verifiedRecipeTotalCalories: " + verifiedRecipeTotalCalories);
                    System.out.println("caloriesDifference " + caloriesDifference);
                    System.out.println("leftCalories " + leftCalories);

                    System.out.println("proteins: " + verifiedRecipe.getRecipeNutrients("Protein"));
                    System.out.println("averageProteins " + (nutrientsScopes.get(mealName).get(0) + 0.025) * totalDailyNutrients.get("Protein"));
                    System.out.println("leftProteins " + leftProteins);
                    System.out.println("proteinsDifference " + proteinsDifference);

                    System.out.println("carbohydrates: " + verifiedRecipe.getRecipeNutrients("Carbohydrate"));
                    System.out.println("averageCarbohydrates " + (nutrientsScopes.get(mealName).get(0) + 0.025) * totalDailyNutrients.get("Carbohydrate"));
                    System.out.println("leftCarbohydrates " + leftCarbohydrates);
                    System.out.println("carbohydratesDifference " + carbohydratesDifference);

                    System.out.println("fats: " + verifiedRecipe.getRecipeNutrients("Fat"));
                    System.out.println("averageFats " + (nutrientsScopes.get(mealName).get(0) + 0.025) * totalDailyNutrients.get("Fat"));
                    System.out.println("leftFats " + leftFats);
                    System.out.println("fatsDifference " + fatsDifference);

                    break;
                }
            }

            if (chosenRecipes.size() < i + 1) {
                return new ResponseMessage("No recipe is appropriate for " + mealName);
            }
        }

        System.out.println();
        System.out.println();

        dailyMenu.setMealsQuantity(mealsQuantity);

        dailyMenu.setDailyMenuName("Day " + currentDay + " of " + lastDay);

        dailyMenu.setDietaryProgramme(dietaryProgramme);

        dailyMenuRepo.save(dailyMenu);

        for (int i = 0; i < chosenRecipes.size(); i++) {
            String mealName = mealNames[i];
            Recipes recipe = chosenRecipes.get(i);
            if (!mealService.addNewMeal(mealName, recipe, dailyMenu).getMessage().equals("Meal has been added")) {

                List<DailyMenu> menus = dailyMenuRepo.findByDietaryProgramme(dietaryProgramme);
                for (DailyMenu menu: menus) {
                    List<Meals> meals = mealRepo.findByDailyMenu(menu);
                    for (Meals meal: meals) {
                        mealRepo.delete(meal);
                    }
                    dailyMenuRepo.delete(menu);
                }

                return new ResponseMessage("Meal has not been created");
            }
        }

        return new ResponseMessage("Daily Menu has been added");
    }

    @Override
    public List<DailyMenuResponse> getDietaryProgrammeDailyMenus(Long dietaryProgrammeId) {

        List<DailyMenuResponse> dietaryProgrammeMenus = new ArrayList<>();
        DietaryProgramme programme = dietaryProgrammeRepo.findById(dietaryProgrammeId).orElse(null);

        if (programme == null) {
            System.out.println("programme");
            return new ArrayList<>();
        }

        List<DailyMenu> menus = dailyMenuRepo.findByDietaryProgramme(programme);

        for (DailyMenu menu : menus) {
            DailyMenuResponse response = new DailyMenuResponse();

            response.setDailyMenuId(menu.getDailyMenuId());
            response.setDailyMenuName(menu.getDailyMenuName());
            response.setDailyMenuDate(menu.getDailyMenuDate());
            response.setMealsQuantity(menu.getMealsQuantity());

            List<Meals> meals = mealRepo.findByDailyMenu(menu);

            for (Meals meal : meals) {
                String mealSentence = "";
                mealSentence += meal.getMealsName() + ";";
                mealSentence += meal.getMealHourTime() + ";";

                Recipes recipe = recipeService.getRecipe(meal.getRecipe().getRecipeId());
                System.out.println(recipe.getRecipeImage());
                mealSentence += recipe.getRecipeImage() + ";";
                mealSentence += recipe.getRecipeName() + ";";

                Double calories = recipe.getRecipeCalories();
                Long averageTemp = Math.round(calories * 100);
                calories = Double.valueOf(averageTemp) / 100;

                mealSentence += calories + ";";

                Double proteins = recipe.getRecipeNutrients("Protein");
                averageTemp = Math.round(proteins * 100);
                proteins = Double.valueOf(averageTemp) / 100;

                mealSentence += proteins + ";";

                Double carbohydrates = recipe.getRecipeNutrients("Carbohydrate");
                averageTemp = Math.round(carbohydrates * 100);
                carbohydrates = Double.valueOf(averageTemp) / 100;

                mealSentence += carbohydrates + ";";

                Double fats = recipe.getRecipeNutrients("Fat");
                averageTemp = Math.round(fats * 100);
                fats = Double.valueOf(averageTemp) / 100;

                mealSentence += fats + ";";

                RecipeGetRequest request = new RecipeGetRequest();
                request.setRecipesGroup("personal");
                request.setPhrase("");
                List<Recipes> collectionRecipes = recipeService.getRecipes(request);

                if (collectionRecipes.contains(recipe)) {
                    mealSentence += "Yes;";
                } else {
                    mealSentence += "No;";
                }

                DietaryPreferences preference = preferenceRepo.findByRelatedDietaryProgramme(programme);

                if (preference == null) {
                    System.out.println("preference");
                    return new ArrayList<>();
                }

                Set<DietaryPreferencesRecipe> recipes = preference.getRecipes();

                Boolean recipePreferred = recipes.stream().filter(preferenceRecipe -> preferenceRecipe.getRecipe().getRecipeId().equals(recipe.getRecipeId())).map(DietaryPreferencesRecipe::isRecipePreferred).findAny().orElse(null);
                if (recipePreferred == null) {
                    mealSentence += "Neutral";
                } else {
                    if (recipePreferred) {
                        mealSentence += "Yes";
                    } else {
                        mealSentence += "No";
                    }
                }

                response.addMeal(mealSentence);
            }

            dietaryProgrammeMenus.add(response);
        }

        return dietaryProgrammeMenus;
    }

    @Override
    public ResponseMessage updateDailyMenu(Long dailyMenuId, DailyMenuRequest dailyMenuRequest) {

        String dailyMenuName = "";
        Optional<DailyMenu> dailyMenu = dailyMenuRepo.findById(dailyMenuId);
        if (dailyMenu.isPresent()) {
            DailyMenu updatedDailyMenu = dailyMenu.get();
            dailyMenuName = dailyMenuRequest.getDailyMenuName();

            ResponseMessage responseMessage = verify("update", "name", dailyMenuName);
            if (responseMessage.getMessage().equals("Daily menu name is valid")) {
                updatedDailyMenu.setDailyMenuName(dailyMenuName);
            } else {
                return responseMessage;
            }

            String dailyMenuDate = dailyMenuRequest.getDailyMenuDate();

            ResponseMessage responseMessage2 = verify("update", "dailyMenuDate", dailyMenuDate);

            if (responseMessage2.getMessage().equals("Daily Menu Date are valid")) {
                updatedDailyMenu.setDailyMenuDate(dailyMenuDate);
            } else {
                return responseMessage2;
            }

            Integer mealsQuantity = dailyMenuRequest.getMealsQuantity();

            ResponseMessage responseMessage3 = verify("update", "mealsQuantity", mealsQuantity);

            if (responseMessage3.getMessage().equals("Product category is valid")) {
                updatedDailyMenu.getMealsQuantity();
            } else {
                return responseMessage3;
            }

            List<String> meals = dailyMenuRequest.getMeals();

            ResponseMessage responseMessage4 = verify("update", "list", meals);

            if (!(responseMessage4.getMessage().equals("Meals are valid"))) {
                return responseMessage4;
            }

            for (String mealsStatement : meals) {

                ResponseMessage responseMessage5 = verify("update", "mealsStatement", mealsStatement);

                if (!(responseMessage5.getMessage().equals("Meals statement is valid"))) {
                    return responseMessage5;
                }
                String[] parts = mealsStatement.split(";");
                String mealsName = parts[0];
//                Double nutrientAmount = Double.valueOf(parts[1]);

                ResponseMessage responseMessage6 = verify("update", "mealsName", mealsName);

                if (!(responseMessage6.getMessage().equals("Meals name is valid"))) {
                    return responseMessage6;
                }

//                ResponseMessage responseMessage7 = verify("update", "nutrientAmount", nutrientAmount);
//
//                if (!(responseMessage7.getMessage().equals("Product nutrient amount is valid"))) {
//                    return responseMessage7;
//                }

//                ProductNutrient productNutrient = updatedProduct.getNutrients().stream().filter(pn -> pn.getNutrient().getNutrientName().equals(nutrientName)).findFirst().orElse(null);
//
//                if (productNutrient != null) {
//                    productNutrient.setNutrientAmount(nutrientAmount);
//                } else {
//                    return new ResponseMessage( "Nutrient " + nutrientName + " belonging to product " + productName + " has not been found");
//                }
            }
            dailyMenuRepo.save(updatedDailyMenu);

            return new ResponseMessage("Daily Menu " + dailyMenuName + " has been updated successfully");
        }
        return new ResponseMessage("Daily Menu " + dailyMenuName + " has not been found");
    }

    @Override
    public ResponseMessage removeDailyMenu(Long dailyMenuId) {

        Optional<DailyMenu> dailyMenu = dailyMenuRepo.findById(dailyMenuId);
        if (dailyMenu.isPresent()) {
            DailyMenu removedDailyMenu = dailyMenu.get();
            dailyMenuRepo.delete(removedDailyMenu);
            return new ResponseMessage("DailyMenu " + removedDailyMenu.getDailyMenuName() + " has been removed successfully");
        }
        return new ResponseMessage("Daily Menu id " + dailyMenuId + " has not been found");
    }
}
