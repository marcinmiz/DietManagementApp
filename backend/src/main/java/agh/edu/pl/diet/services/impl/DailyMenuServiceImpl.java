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
import agh.edu.pl.diet.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DailyMenuServiceImpl implements DailyMenuService, MenusAndMealsService {

    @Autowired
    private DailyMenuRepo dailyMenuRepo;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private MealService mealService;

    @Autowired
    private UserService userService;

    @Autowired
    private MealRepo mealRepo;

    @Autowired
    private DietaryProgrammeRepo dietaryProgrammeRepo;

    @Autowired
    private DietaryPreferencesRepo preferenceRepo;

    @Override
    public ResponseMessage verifyRecipe(Recipes recipe, Map<String, List<Double>> dailyNutrientsScopes, DietaryPreferences preference) {

        User loggedUser = userService.findByUsername(userService.getLoggedUser().getUsername());

        if (loggedUser == null) {
            return new ResponseMessage("Logged user has not been found");
        }

        if (preference == null) {
            return new ResponseMessage("Dietary programme has to be related to dietary preference");
        }

        String failExplanation = "";

        System.out.println(recipe.getRecipeName() + " verification");

        Double ultimateForce = 1.0;

        Set<String> keySet = dailyNutrientsScopes.keySet();

        String[] nutrientNames
                = keySet.toArray(new String[keySet.size()]);

        for (int i = 0; i < nutrientNames.length; i++) {

            String nutrientName = nutrientNames[i];

            Double nutrientAmount = recipe.getRecipeNutrients(nutrientName);
            Double lowerBound = dailyNutrientsScopes.get(nutrientName).get(0);
            Double upperBound = dailyNutrientsScopes.get(nutrientName).get(1);

            Long tempLowerBound = Math.round(lowerBound * 100);

            lowerBound = Double.valueOf(tempLowerBound) / 100;

            Long tempUpperBound = Math.round(upperBound * 100);

            upperBound = Double.valueOf(tempUpperBound) / 100;

            Long tempNutrientAmount = Math.round(nutrientAmount * 100);

            nutrientAmount = Double.valueOf(tempNutrientAmount) / 100;

            if (nutrientAmount < lowerBound) {
                System.out.println(nutrientName + ": actual: " + nutrientAmount + ", lower: " + lowerBound);
                return new ResponseMessage(nutrientName + " " + nutrientAmount + " are less than " + lowerBound);
            } else if (nutrientAmount > upperBound) {
                System.out.println(nutrientName + ": actual: " + nutrientAmount + ", upper: " + upperBound);
                return new ResponseMessage(nutrientName + " " + nutrientAmount + " are more than " + upperBound);
            }

        }

//        RecipeGetRequest request = new RecipeGetRequest();
//        request.setRecipesGroup("personal");
//        request.setPhrase("");
//        List<Recipes> collectionRecipes = recipeService.getRecipes(request);

        long recipeId = recipe.getRecipeId();
        ResponseMessage message = recipeService.checkIfInCollection(recipeId);

        if (message.getMessage().equals("Recipe with id " + recipeId + " has not been found")) {
            return message;
        }

        if (message.getMessage().equals("Recipe " + recipe.getRecipeName() + " with id " + recipeId + " is in collection"))
            ultimateForce *= 0.7;
        else
            ultimateForce *= 0.3;

        System.out.println("ultimateForce after collection check: " + ultimateForce);

        if (ultimateForce < 0.15) {
            failExplanation = recipe.getRecipeName() + " not in user's recipe collection";
        }

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
                Double dietImprovement = loggedUser.getDietImprovement();
                if (dietImprovement != null) {
                    ultimateForce *= dietImprovement;
                }
                ultimateForce *= 0.15;
                break;
            default:
                ultimateForce *= 1.0;
        }

        System.out.println("ultimateForce after pyramid check: " + ultimateForce);

        if (ultimateForce >= 0.15) {
            failExplanation = "";
        } else {
            failExplanation = recipe.getRecipeName() + " contains most " + majorCategoryProductName + " category products";
        }

        List<RecipeCustomerSatisfaction> recipeSatisfactions = recipe.getRecipeCustomerSatisfactions();
        List<RecipeCustomerSatisfaction> recipeRatings = recipeSatisfactions.stream().filter(satisfaction -> satisfaction.getRecipeRating() != null).collect(Collectors.toList());

        if (!recipeRatings.isEmpty()) {
            OptionalDouble average = recipeRatings.stream().mapToDouble(RecipeCustomerSatisfaction::getRecipeRating).average();
            if (average.isPresent()) {
                Long averageTempRating = Math.round(average.getAsDouble() * 1000);

                Double averageRating = Double.valueOf(averageTempRating) / 1000;

                if (averageRating < 3.0) {
                    ultimateForce *= 0.0;
                    failExplanation = recipe.getRecipeName() + "'s average rating is less than 3.0";
                }

            }
        }

        System.out.println("ultimateForce after ratings check: " + ultimateForce);

        List<RecipeCustomerSatisfaction> recipeFavourites = recipeSatisfactions.stream().filter(satisfaction -> satisfaction.getRecipeFavourite() != null).collect(Collectors.toList());

        if (!recipeFavourites.isEmpty()) {
            for (RecipeCustomerSatisfaction satisfaction : recipeFavourites) {
                if (satisfaction.getCustomerSatisfactionOwner().getUserId().equals(loggedUser.getUserId()) && satisfaction.getRecipeFavourite()) {
                    ultimateForce *= 3.0;
                }
            }
        }

        System.out.println("ultimateForce after favourite check: " + ultimateForce);

        if (ultimateForce >= 0.15) {
            failExplanation = "";
        }

//        DietaryPreferences preference = preferenceRepo.findByRelatedDietaryProgramme(dietaryProgramme);

        String recipePreferred = "neutral";
        Set<DietaryPreferencesRecipe> preferredRecipes = preference.getRecipes();

        for (DietaryPreferencesRecipe preferredRecipe : preferredRecipes) {
            if (preferredRecipe.getRecipe().getRecipeId().equals(recipe.getRecipeId())) {
                if (preferredRecipe.isRecipePreferred()) {
                    recipePreferred = "liked";
                    ultimateForce *= 3.0;
                } else {
                    recipePreferred = "disliked";
                    ultimateForce *= 0.0;
                    failExplanation = recipe.getRecipeName() + " is disliked";
                }
            }

        }

        System.out.println("ultimateForce after preferred recipes check: " + ultimateForce);

        if (ultimateForce >= 0.15) {
            failExplanation = "";
        }

        Set<DietaryPreferencesProduct> preferredProducts = preference.getProducts();

        List<Product> ingredients = recipeProducts.stream().map(RecipeProduct::getProduct).collect(Collectors.toList());

        for (DietaryPreferencesProduct preferredProduct : preferredProducts) {

            System.out.println(recipePreferred);
            if (ingredients.contains(preferredProduct.getProduct())) {
                if (preferredProduct.isProductPreferred()) {
                    ultimateForce *= 2.0;
                } else if (!recipePreferred.equals("liked")) {
                    ultimateForce *= 0.0;
                    failExplanation = "Product " + preferredProduct.getProduct().getProductName() + " is disliked";
                }
            }

        }

        System.out.println("ultimateForce after preferred products check(end): " + ultimateForce);

        if (ultimateForce >= 0.15) {
            failExplanation = "";
        }

        if (ultimateForce < 0.15) {
            String explanation = failExplanation.equals("") ? "" : " (" + failExplanation + ")";
            return new ResponseMessage("Recipe is inappropriate in regard to dietary preference" + explanation);
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
        request.setAll("yes");
        request.setGroupNumber(0);
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

                Collections.shuffle(potentialRecipes); //shuffle potentialRecipes to get random order

                System.out.println("id");
                potentialRecipes.stream().map(Recipes::getRecipeId).forEach(System.out::println);
            }

            for (Recipes verifiedRecipe : potentialRecipes) {

                if (verifyRecipe(verifiedRecipe, dailyNutrientsScopes, preferenceRepo.findByRelatedDietaryProgramme(dietaryProgramme)).getMessage().equals("Recipe is appropriate in regard to dietary preference") && !chosenRecipes.contains(verifiedRecipe)) {
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

        System.out.println(dailyMenu.getDailyMenuName());

        dailyMenuRepo.save(dailyMenu);

        for (int i = 0; i < chosenRecipes.size(); i++) {
            String mealName = mealNames[i];
            Recipes recipe = chosenRecipes.get(i);
            if (!mealService.addNewMeal(mealName, recipe, dailyMenu).getMessage().equals("Meal has been added")) {

                removeDailyMenusWithMeals(dietaryProgramme);

                return new ResponseMessage("Meal has not been created");
            }
        }

        return new ResponseMessage("Daily Menu has been added");
    }

    @Override
    public List<DailyMenuResponse> getDietaryProgrammeDailyMenus(Long dietaryProgrammeId) {

        User loggedUser = userService.findByUsername(userService.getLoggedUser().getUsername());

        if (loggedUser == null) {
            return new ArrayList<>();
        }

        List<DailyMenuResponse> dietaryProgrammeMenus = new ArrayList<>();
        DietaryProgramme programme = dietaryProgrammeRepo.findById(dietaryProgrammeId).orElse(null);

        if (programme == null) {
            System.out.println("programme");
            return new ArrayList<>();
        }

        List<DailyMenu> menus = dailyMenuRepo.findByDietaryProgramme(programme);

        menus.sort((m1, m2) -> {
            Integer dayNumber1 = Integer.valueOf(m1.getDailyMenuName().split(" ")[1]);
            Integer dayNumber2 = Integer.valueOf(m2.getDailyMenuName().split(" ")[1]);
            return dayNumber1.compareTo(dayNumber2);
        });

        System.out.println();
        menus.forEach(menu1 -> System.out.println(menu1.getDailyMenuName()));
        System.out.println();

        for (DailyMenu menu : menus) {
            DailyMenuResponse response = new DailyMenuResponse(menu.getDailyMenuId(), menu.getDailyMenuName(), menu.getDailyMenuDate(), menu.getMealsQuantity());

            List<Meals> meals = mealRepo.findByDailyMenu(menu);

            Map<Integer, Meals> orderedMealsTree = new TreeMap<>();

            List<String> listOrder = Arrays.asList("breakfast", "lunch", "dinner", "tea", "supper");

            for (Meals meal : meals) {
                orderedMealsTree.put(listOrder.indexOf(meal.getMealsName()), meal);
            }

            List<Meals> orderedMealsList = new ArrayList<>(orderedMealsTree.values());

            System.out.println();
            orderedMealsList.forEach(meals1 -> System.out.println(meals1.getMealsName()));
            System.out.println();

            for (Meals meal : orderedMealsList) {

                Recipes recipe = recipeService.getRecipe(meal.getRecipe().getRecipeId());

                String recipeAuthor = recipe.getRecipeOwner().getName() + " " + recipe.getRecipeOwner().getSurname();

                Double calories = recipe.getRecipeCalories();
                Long averageTemp = Math.round(calories * 100);
                calories = Double.valueOf(averageTemp) / 100;

                Double proteins = recipe.getRecipeNutrients("Protein");
                averageTemp = Math.round(proteins * 100);
                proteins = Double.valueOf(averageTemp) / 100;

                Double carbohydrates = recipe.getRecipeNutrients("Carbohydrate");
                averageTemp = Math.round(carbohydrates * 100);
                carbohydrates = Double.valueOf(averageTemp) / 100;

                Double fats = recipe.getRecipeNutrients("Fat");
                averageTemp = Math.round(fats * 100);
                fats = Double.valueOf(averageTemp) / 100;

                String inCollection;
                ResponseMessage message = recipeService.checkIfInCollection(recipe.getRecipeId());

                if (message.getMessage().equals("Recipe " + recipe.getRecipeName() + " with id " + recipe.getRecipeId() + " is in collection")) {
                    inCollection = "Yes";
                } else {
                    inCollection = "No";
                }

                String likedInPreference;
                DietaryPreferences preference = preferenceRepo.findByRelatedDietaryProgramme(programme);

                if (preference == null) {
                    System.out.println("preference");
                    return new ArrayList<>();
                }

                Set<DietaryPreferencesRecipe> recipes = preference.getRecipes();

                Boolean recipePreferred = recipes.stream().filter(preferenceRecipe -> preferenceRecipe.getRecipe().getRecipeId().equals(recipe.getRecipeId())).map(DietaryPreferencesRecipe::isRecipePreferred).findAny().orElse(null);
                if (recipePreferred == null) {
                    likedInPreference = "Neutral";
                } else {
                    if (recipePreferred) {
                        likedInPreference = "Yes";
                    } else {
                        likedInPreference = "No";
                    }
                }

                String favourite = "No";

                List<RecipeCustomerSatisfaction> customerSatisfactions = recipe.getRecipeCustomerSatisfactions().stream().filter(satisfaction -> satisfaction.getCustomerSatisfactionOwner().getUserId().equals(loggedUser.getUserId())).collect(Collectors.toList());

                if (!customerSatisfactions.isEmpty()) {
                    if (customerSatisfactions.get(0).getRecipeFavourite()) {
                        favourite = "Yes";
                    } else {
                        favourite = "No";
                    }
                }

                DailyMenuResponse.Recipe createdRecipe = response.createRecipe(recipe.getRecipeId(), recipe.getRecipeName(), recipe.getCreationDate(), recipe.getRecipeOwner().getUserId(), recipeAuthor, recipe.getRecipeOwner().getAvatarImage(), recipe.getRecipeImage(), calories, proteins, carbohydrates, fats, inCollection, likedInPreference, favourite, recipe.getRecipeProducts(), recipe.getRecipeSteps());

                response.addMeal(meal.getMealId(), meal.getMealsName(), meal.getMealHourTime(), createdRecipe, meal.getConsumed());
            }

            dietaryProgrammeMenus.add(response);
        }

        return dietaryProgrammeMenus;
    }

    @Override
    public Boolean removeDailyMenusWithMeals(DietaryProgramme programme) {

        List<DailyMenu> menus = dailyMenuRepo.findByDietaryProgramme(programme);
        for (DailyMenu menu : menus) {
            List<Meals> meals = mealRepo.findByDailyMenu(menu);
            for (Meals meal : meals) {
                mealRepo.delete(meal);
            }
            dailyMenuRepo.delete(menu);
        }
        return true;
    }
}
