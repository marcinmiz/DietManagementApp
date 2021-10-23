package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.*;
import agh.edu.pl.diet.payloads.request.DietaryPreferencesRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.*;
import agh.edu.pl.diet.services.DietaryPreferencesService;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DietaryPreferencesServiceImpl implements DietaryPreferencesService {

    @Autowired
    private NutrientRepo nutrientRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private RecipeRepo recipeRepo;
    @Autowired
    private DietTypeRepo dietTypeRepo;
    @Autowired
    private DietaryPreferencesRepo dietaryPreferencesRepo;
    @Autowired
    private DietaryPreferencesProductsRepo dietaryPreferencesProductsRepo;
    @Autowired
    private DietaryPreferencesRecipesRepo dietaryPreferencesRecipesRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;

    private ResponseMessage verify(String type, Object item) {
        switch (type) {
            case "dietTypeSelected":
                if (item == null) {
                    return new ResponseMessage("Diet type selected has to be given");
                } else {
                    return new ResponseMessage("Diet type selected is valid");
                }
            case "totalDailyCalories":
                if (item == null) {
                    return new ResponseMessage("Dietary Preferences total daily calories has to be given");
                }

                Double totalDailyCalories = Double.parseDouble(item.toString());

                if (totalDailyCalories.toString().length() < 1 || totalDailyCalories.toString().length() > 6) {
                    return new ResponseMessage("Dietary Preferences total daily calories has to have min 1 and max 6 characters");
                } else if (!(totalDailyCalories.toString().matches("^0(.\\d+)?$") || totalDailyCalories.toString().matches("^(-)?[1-9]\\d*(.\\d+)?$"))) {
                    return new ResponseMessage("Dietary Preferences total daily calories has to contain only digits");
                } else if (totalDailyCalories < 0) {
                    return new ResponseMessage("Dietary Preferences total daily calories has to be greater or equal 0");
                } else if (totalDailyCalories > 100000) {
                    return new ResponseMessage("Dietary Preferences total daily calories has to be less or equal 100 000");
                } else {
                    return new ResponseMessage("Total daily calories are valid");
                }
            case "dietType":
                if (item == null) {
                    return new ResponseMessage("Diet type has to be given");
                }

                String dietTypeName = String.valueOf(item);

                if (dietTypeName.equals("")) {
                    return new ResponseMessage("Diet type has to be chosen");
                } else if (dietTypeRepo.findByDietTypeName(dietTypeName) == null) {
                    return new ResponseMessage("Diet type does not exist");
                } else {
                    return new ResponseMessage("Diet type is valid");
                }
            case "caloriesPerMeal":
                if (item == null) {
                    return new ResponseMessage("Calories per meal has to be given");
                }

                Integer caloriesPerMeal = Integer.parseInt(item.toString());

                if (caloriesPerMeal.toString().length() < 1 || caloriesPerMeal.toString().length() > 5) {
                    return new ResponseMessage("Calories per meal has to have min 1 and max 5 characters");
                } else if (!(caloriesPerMeal.toString().matches("^0(.\\d+)?$") || caloriesPerMeal.toString().matches("^(-)?[1-9]\\d*(.\\d+)?$"))) {
                    return new ResponseMessage("Calories per meal has to contain only digits");
                } else if (caloriesPerMeal < 0) {
                    return new ResponseMessage("Calories per meal has to be greater or equal 0");
                } else if (caloriesPerMeal > 50000) {
                    return new ResponseMessage("Calories per meal has to be less or equal 50000");
                } else {
                    return new ResponseMessage("Calories per meal are valid");
                }
            case "mealsQuantity":
                if (item == null) {
                    return new ResponseMessage("Meals quantity has to be given");
                }

                Integer mealsQuantity = Integer.parseInt(item.toString());

                if (mealsQuantity.toString().length() < 1 || mealsQuantity.toString().length() > 2) {
                    return new ResponseMessage("Meals quantity has to have min 1 and max 2 characters");
                } else if (!(mealsQuantity.toString().matches("^0$") || mealsQuantity.toString().matches("^(-)?[1-9]\\d*$"))) {
                    return new ResponseMessage("Meals quantity has to contain only digits");
                } else if (mealsQuantity < 0) {
                    return new ResponseMessage("Meals quantity has to be greater or equal 0");
                } else if (mealsQuantity > 10) {
                    return new ResponseMessage("Meals quantity has to be less or equal 10");
                } else {
                    return new ResponseMessage("Meals quantity is valid");
                }
            case "targetWeight":
                if (item == null) {
                    return new ResponseMessage("Target weight has to be given");
                }

                Double weight = Double.parseDouble(item.toString());
                Integer targetWeight = weight.intValue();

                if (targetWeight.toString().length() < 1 || targetWeight.toString().length() > 3) {
                    return new ResponseMessage("Target weight has to have min 1 and max 3 characters");
                } else if (!(targetWeight.toString().matches("^0(.\\d+)?$") || targetWeight.toString().matches("^(-)?[1-9]\\d*(.\\d+)?$"))) {
                    return new ResponseMessage("Target weight has to contain only digits");
                } else if (targetWeight < 0) {
                    return new ResponseMessage("Target weight has to be greater or equal 0");
                } else if (targetWeight > 500) {
                    return new ResponseMessage("Target weight has to be less or equal 500");
                } else {
                    return new ResponseMessage("Target weight is valid");
                }
            case "nutrients":
                List<String> nutrients = (List<String>) item;
                if (nutrients == null) {
                    return new ResponseMessage("Preference nutrients has to be given");
                } else if (nutrients.isEmpty()) {
                    return new ResponseMessage("At least 1 preference nutrient is required");
                } else {
                    return new ResponseMessage("Preference nutrients are valid");
                }
            case "nutrientStatement":
                String nutrientStatement = String.valueOf(item);
                if (nutrientStatement.equals("")) {
                    return new ResponseMessage("Preference nutrient has to be defined");
                } else if (!(nutrientStatement.matches("^[a-zA-Z]+;0(.\\d+)?;[<>=]$") || nutrientStatement.matches("^[a-zA-Z]+;(-)?[1-9]\\d*(.\\d+)?;[<>=]$"))) {
                    return new ResponseMessage("Preference nutrient has to match format \"productName;productAmount;nutrientRelation\", where nutrientRelation might be \">\", \"<\" or \"=\"");
                } else {
                    return new ResponseMessage("Preference nutrient statement is valid");
                }
            case "nutrientName":
                String nutrientName = String.valueOf(item);

                if (nutrientRepo.findByNutrientName(nutrientName) == null) {
                    return new ResponseMessage("Preference nutrient name has to be proper");
                } else {
                    return new ResponseMessage("Preference nutrient name is valid");
                }
            case "nutrientAmount":
                Double amount = Double.parseDouble(item.toString());
                Integer nutrientAmount = amount.intValue();

                if (nutrientAmount.toString().length() < 1 || nutrientAmount.toString().length() > 6) {
                    return new ResponseMessage("Preference nutrient amount has to have min 1 and max 6 characters");
                } else if (nutrientAmount < 0) {
                    return new ResponseMessage("Preference nutrient amount has to be greater or equal 0");
                } else if (nutrientAmount > 100000) {
                    return new ResponseMessage("Preference nutrient amount has to be less or equal 100000");
                } else {
                    return new ResponseMessage("Preference nutrient amount is valid");
                }
            case "products":
                List<String> products = (List<String>) item;
                if (products == null) {
                    return new ResponseMessage("Preference products has to be given");
                } else {
                    return new ResponseMessage("Preference products are valid");
                }
            case "productStatement":

                if (item.equals("")) {
                    return new ResponseMessage("Preference product has to be defined");
                }

                String productStatement = String.valueOf(item);

                if (!productStatement.matches("^[a-zA-Z ]+;[a-zA-Z]+$")) {
                    return new ResponseMessage("Preference product has to match format \"productName;productPreferred\"");
                } else {
                    return new ResponseMessage("Preference product statement is valid");
                }
            case "productName":
                String productName = String.valueOf(item);

                if (productRepo.findByProductName(productName) == null) {
                    return new ResponseMessage("Preference product name has to be proper");
                } else {
                    return new ResponseMessage("Preference product name is valid");
                }
            case "productPreferred":
                String productPreferred = String.valueOf(item);

                if (!productPreferred.equalsIgnoreCase("true") && !productPreferred.equalsIgnoreCase("false")) {
                    return new ResponseMessage("Preference product preferred value has to be \"true\" or \"false\"");
                } else {
                    return new ResponseMessage("Preference product preferred is valid");
                }
            case "recipes":
                List<String> recipes = (List<String>) item;
                if (recipes == null) {
                    return new ResponseMessage("Preference recipes has to be given");
                } else {
                    return new ResponseMessage("Preference recipes are valid");
                }
            case "recipeStatement":
                String recipeStatement = String.valueOf(item);
                if (recipeStatement.equals("")) {
                    return new ResponseMessage("Preference recipe has to be defined");
                } else if (!recipeStatement.matches("^[a-zA-Z ]+;[a-zA-Z]+$")) {
                    return new ResponseMessage("Preference recipe has to match format \"recipeName;recipePreferred\"");
                } else {
                    return new ResponseMessage("Preference recipe statement is valid");
                }
            case "recipeName":
                String recipeName = String.valueOf(item);

                if (recipeRepo.findByRecipeName(recipeName) == null) {
                    return new ResponseMessage("Preference recipe name has to be proper");
                } else {
                    return new ResponseMessage("Preference recipe name is valid");
                }
            case "recipePreferred":
                String recipePreferred = String.valueOf(item);

                if (!recipePreferred.equalsIgnoreCase("true") && !recipePreferred.equalsIgnoreCase("false")) {
                    return new ResponseMessage("Preference recipe preferred value has to be \"true\" or \"false\"");
                } else {
                    return new ResponseMessage("Preference recipe preferred is valid");
                }

        }

        return new ResponseMessage("Invalid type");
    }

    @Override
    public List<DietaryPreferences> getUserDietaryPreferences() {
        User currentLoggedUser = userService.findByUsername(userService.getLoggedUser().getUsername());
        if (currentLoggedUser == null) {
            return null;
        }
        List<DietaryPreferences> list = new ArrayList<>();
        dietaryPreferencesRepo.findAll().forEach(list::add);
        list = list.stream().filter(dietaryPreferences -> dietaryPreferences.getPreferenceOwner().getUserId().equals(currentLoggedUser.getUserId())).collect(Collectors.toList());
//        for (DietaryPreferences preference: list) {
//            Set<DietaryPreferencesProduct> products = preference.getProducts();
//            products.
//        }
        return list;
    }

    @Override
    public DietaryPreferences getDietaryPreferences(Long dietaryPreferencesId) {
        return dietaryPreferencesRepo.findById(dietaryPreferencesId).orElse(null);
    }

    @Override
    public ResponseMessage addNewDietaryPreferences(DietaryPreferencesRequest dietaryPreferencesRequest) {
        DietaryPreferences dietaryPreference = new DietaryPreferences();
        Boolean dietTypeSelected = dietaryPreferencesRequest.getDietTypeSelected();

        ResponseMessage responseMessage = verify("dietTypeSelected", dietTypeSelected);

        if (!responseMessage.getMessage().equals("Diet type selected is valid")) {
            return responseMessage;
        }

        Double totalDailyCalories = Double.parseDouble(dietaryPreferencesRequest.getTotalDailyCalories());

        ResponseMessage responseMessage2 = verify("totalDailyCalories", totalDailyCalories);

        if (responseMessage2.getMessage().equals("Total daily calories are valid")) {
            dietaryPreference.setTotalDailyCalories(totalDailyCalories);
        } else {
            return responseMessage2;
        }
        dietaryPreference.setTotalDailyCalories(totalDailyCalories);

        if (dietTypeSelected) {
            String dietTypeName = dietaryPreferencesRequest.getDietType();

            ResponseMessage responseMessage3 = verify("dietType", dietTypeName);

            if (responseMessage3.getMessage().equals("Diet type is valid")) {
                dietaryPreference.setDietType(dietTypeRepo.findByDietTypeName(dietTypeName));
            } else {
                return responseMessage3;
            }

        }

        Integer caloriesPerMeal = dietaryPreferencesRequest.getCaloriesPerMeal();

        ResponseMessage responseMessage4 = verify("caloriesPerMeal", caloriesPerMeal);

        if (responseMessage4.getMessage().equals("Calories per meal are valid")) {
            dietaryPreference.setCaloriesPerMeal(caloriesPerMeal);
        } else {
            return responseMessage4;
        }
        dietaryPreference.setCaloriesPerMeal(caloriesPerMeal);

        Integer mealsQuantity = dietaryPreferencesRequest.getMealsQuantity();

        ResponseMessage responseMessage5 = verify("mealsQuantity", mealsQuantity);

        if (responseMessage5.getMessage().equals("Meals quantity is valid")) {
            dietaryPreference.setMealsQuantity(mealsQuantity);
        } else {
            return responseMessage5;
        }

        dietaryPreference.setMealsQuantity(mealsQuantity);

        Double targetWeight = dietaryPreferencesRequest.getTargetWeight();

        ResponseMessage responseMessage6 = verify("targetWeight", targetWeight);

        if (responseMessage6.getMessage().equals("Target weight is valid")) {
            dietaryPreference.setTargetWeight(targetWeight);
        } else {
            return responseMessage6;
        }

        dietaryPreference.setTargetWeight(targetWeight);

        List<String> nutrients = dietaryPreferencesRequest.getNutrients();

        ResponseMessage responseMessage7 = verify("nutrients", nutrients);

        if (!(responseMessage7.getMessage().equals("Preference nutrients are valid"))) {
            return responseMessage7;
        }

        for (String nutrientStatement: nutrients) {

            ResponseMessage responseMessage8 = verify("nutrientStatement", nutrientStatement);

            if (!(responseMessage8.getMessage().equals("Preference nutrient statement is valid"))) {
                return responseMessage8;
            }

            String[] parts = nutrientStatement.split(";");
            String nutrientName = parts[0];
            Double nutrientAmount = Double.valueOf(parts[1]);
            String nutrientRelation = parts[2];

            ResponseMessage responseMessage9 = verify("nutrientName", nutrientName);

            if (!(responseMessage9.getMessage().equals("Preference nutrient name is valid"))) {
                return responseMessage9;
            }

            ResponseMessage responseMessage10 = verify("nutrientAmount", nutrientAmount);

            if (!(responseMessage10.getMessage().equals("Preference nutrient amount is valid"))) {
                return responseMessage10;
            }

            Nutrient nutrient = nutrientRepo.findByNutrientName(nutrientName);
            DietaryPreferencesNutrient dietaryPreferencesNutrient = new DietaryPreferencesNutrient();
            dietaryPreferencesNutrient.setNutrient(nutrient);
            dietaryPreferencesNutrient.setNutrientAmount(nutrientAmount);
            dietaryPreferencesNutrient.setNutrientRelation(nutrientRelation);
            dietaryPreferencesNutrient.setDietaryPreferences(dietaryPreference);
            dietaryPreference.addNutrient(dietaryPreferencesNutrient);
        }

        List<String> products = dietaryPreferencesRequest.getProducts();

        ResponseMessage responseMessage12 = verify("products", products);

        if (!(responseMessage12.getMessage().equals("Preference products are valid"))) {
            return responseMessage12;
        }

        for (String productStatement: products) {

            ResponseMessage responseMessage13 = verify("productStatement", productStatement);

            if (!(responseMessage13.getMessage().equals("Preference product statement is valid"))) {
                return responseMessage13;
            }

            String[] parts = productStatement.split(";");
            String productName = parts[0];
            String productPreferred = parts[1];

            ResponseMessage responseMessage14 = verify("productName", productName);

            if (!(responseMessage14.getMessage().equals("Preference product name is valid"))) {
                return responseMessage14;
            }

            ResponseMessage responseMessage15 = verify("productPreferred", productPreferred);

            if (!(responseMessage15.getMessage().equals("Preference product preferred is valid"))) {
                return responseMessage15;
            }

            Product product = productRepo.findByProductName(productName);
            DietaryPreferencesProduct dietaryPreferencesProduct = new DietaryPreferencesProduct();
            dietaryPreferencesProduct.setProduct(product);
            dietaryPreferencesProduct.setProductPreferred(productPreferred.equalsIgnoreCase("true"));
            dietaryPreferencesProduct.setDietaryPreferences(dietaryPreference);
            dietaryPreference.addProduct(dietaryPreferencesProduct);
        }

        List<String> recipes = dietaryPreferencesRequest.getRecipes();

        ResponseMessage responseMessage16 = verify("recipes", recipes);

        if (!(responseMessage16.getMessage().equals("Preference recipes are valid"))) {
            return responseMessage16;
        }

        for (String recipeStatement: recipes) {

            ResponseMessage responseMessage17 = verify("recipeStatement", recipeStatement);

            if (!(responseMessage17.getMessage().equals("Preference recipe statement is valid"))) {
                return responseMessage17;
            }

            String[] parts = recipeStatement.split(";");
            String recipeName = parts[0];
            String recipePreferred = parts[1];

            ResponseMessage responseMessage18 = verify("recipeName", recipeName);

            if (!(responseMessage18.getMessage().equals("Preference recipe name is valid"))) {
                return responseMessage18;
            }

            ResponseMessage responseMessage19 = verify("recipePreferred", recipePreferred);

            if (!(responseMessage19.getMessage().equals("Preference recipe preferred is valid"))) {
                return responseMessage19;
            }

            Recipes recipe = recipeRepo.findByRecipeName(recipeName);
            DietaryPreferencesRecipe dietaryPreferencesRecipe = new DietaryPreferencesRecipe();
            dietaryPreferencesRecipe.setRecipe(recipe);
            dietaryPreferencesRecipe.setRecipePreferred(recipePreferred.equalsIgnoreCase("true"));
            dietaryPreferencesRecipe.setDietaryPreferences(dietaryPreference);
            dietaryPreference.addRecipe(dietaryPreferencesRecipe);
        }

        //change to logged in user id
        User preferenceOwner = userService.findByUsername(userService.getLoggedUser().getUsername());
        if (preferenceOwner == null) {
            return new ResponseMessage("Current logged user has not been found");
        }
        dietaryPreference.setPreferenceOwner(preferenceOwner);

        String creationDate = new Date().toInstant().toString();
        dietaryPreference.setCreationDate(creationDate);

        dietaryPreferencesRepo.save(dietaryPreference);
//        DietaryPreferences lastAddedDietaryPreferences = getDietaryPreferences().stream().filter(p -> p.getCreationDate().equals(creationDate)).findFirst().orElse(null);
//        if (lastAddedDietaryPreferences != null) {
//            return new ResponseMessage(lastAddedDietaryPreferences.getDietaryPreferenceId() + " Dietary Preference " + dietaryPreferencesName + " has been added successfully");
//        } else {
//          return new ResponseMessage("Dietary Preference " + dietaryPreferenceId + " has not been found");
//        }
        return new ResponseMessage("Dietary Preference has been added");
    }

    @Override
    public ResponseMessage updateDietaryPreferences(Long dietaryPreferenceId, DietaryPreferencesRequest dietaryPreferencesRequest) {

        Optional<DietaryPreferences> dietaryPreference = dietaryPreferencesRepo.findById(dietaryPreferenceId);
        if (dietaryPreference.isPresent()) {
            DietaryPreferences updatedDietaryPreference = dietaryPreference.get();

            Boolean dietTypeSelected = dietaryPreferencesRequest.getDietTypeSelected();

            ResponseMessage responseMessage = verify("dietTypeSelected", dietTypeSelected);

            if (!responseMessage.getMessage().equals("Diet type selected is valid")) {
                return responseMessage;
            }

            Double totalDailyCalories = Double.parseDouble(dietaryPreferencesRequest.getTotalDailyCalories());

            ResponseMessage responseMessage2 = verify("totalDailyCalories", totalDailyCalories);

            if (responseMessage2.getMessage().equals("Total daily calories are valid")) {
                updatedDietaryPreference.setTotalDailyCalories(totalDailyCalories);
            } else {
                return responseMessage2;
            }
            updatedDietaryPreference.setTotalDailyCalories(totalDailyCalories);

            if (dietTypeSelected) {

                String dietTypeName = dietaryPreferencesRequest.getDietType();

                ResponseMessage responseMessage3 = verify("dietType", dietTypeName);

                if (responseMessage3.getMessage().equals("Diet type is valid")) {
                    updatedDietaryPreference.setDietType(dietTypeRepo.findByDietTypeName(dietTypeName));
                } else {
                    return responseMessage3;
                }

                updatedDietaryPreference.setDietType(dietTypeRepo.findByDietTypeName(dietTypeName));

            }

            Integer caloriesPerMeal = dietaryPreferencesRequest.getCaloriesPerMeal();

            ResponseMessage responseMessage4 = verify("caloriesPerMeal", caloriesPerMeal);

            if (responseMessage4.getMessage().equals("Calories per meal are valid")) {
                updatedDietaryPreference.setCaloriesPerMeal(caloriesPerMeal);
            } else {
                return responseMessage4;
            }
            updatedDietaryPreference.setCaloriesPerMeal(caloriesPerMeal);

            Integer mealsQuantity = dietaryPreferencesRequest.getMealsQuantity();

            ResponseMessage responseMessage5 = verify("mealsQuantity", mealsQuantity);

            if (responseMessage5.getMessage().equals("Meals quantity is valid")) {
                updatedDietaryPreference.setMealsQuantity(mealsQuantity);
            } else {
                return responseMessage5;
            }

            updatedDietaryPreference.setMealsQuantity(mealsQuantity);

            Double targetWeight = dietaryPreferencesRequest.getTargetWeight();

            ResponseMessage responseMessage6 = verify("targetWeight", targetWeight);

            if (responseMessage6.getMessage().equals("Target weight is valid")) {
                updatedDietaryPreference.setTargetWeight(targetWeight);
            } else {
                return responseMessage6;
            }

            updatedDietaryPreference.setTargetWeight(targetWeight);

            List<String> nutrients = dietaryPreferencesRequest.getNutrients();

            ResponseMessage responseMessage7 = verify("nutrients", nutrients);

            if (!(responseMessage7.getMessage().equals("Preference nutrients are valid"))) {
                return responseMessage7;
            }

            for (String nutrientStatement: nutrients) {

                ResponseMessage responseMessage8 = verify("nutrientStatement", nutrientStatement);

                if (!(responseMessage8.getMessage().equals("Preference nutrient statement is valid"))) {
                    return responseMessage8;
                }
                String[] parts = nutrientStatement.split(";");
                String nutrientName = parts[0];
                Double nutrientAmount = Double.valueOf(parts[1]);

                ResponseMessage responseMessage9 = verify("nutrientName", nutrientName);

                if (!(responseMessage9.getMessage().equals("Preference nutrient name is valid"))) {
                    return responseMessage9;
                }

                ResponseMessage responseMessage10 = verify("nutrientAmount", nutrientAmount);

                if (!(responseMessage10.getMessage().equals("Preference nutrient amount is valid"))) {
                    return responseMessage10;
                }

                DietaryPreferencesNutrient dietaryPreferencesNutrient = updatedDietaryPreference.getNutrients().stream().filter(pn -> pn.getNutrient().getNutrientName().equals(nutrientName)).findFirst().orElse(null);

                if (dietaryPreferencesNutrient != null) {
                    dietaryPreferencesNutrient.setNutrientAmount(nutrientAmount);
                } else {
                    return new ResponseMessage( "Nutrient " + nutrientName + " belonging to product " + dietaryPreferenceId + " has not been found");
                }
            }

            List<String> preferredProducts = dietaryPreferencesRequest.getProducts();
            List<DietaryPreferencesProduct> currentPreferredProducts = new ArrayList<>();
            currentPreferredProducts.addAll(updatedDietaryPreference.getProducts());
            Set<DietaryPreferencesProduct> products = new HashSet<>();

            ResponseMessage responseMessage11 = verify("products", preferredProducts);

            if (!(responseMessage11.getMessage().equals("Preference products are valid"))) {
                return responseMessage11;
            }

            for (String productStatement: preferredProducts) {

                ResponseMessage responseMessage12 = verify("productStatement", productStatement);

                if (!(responseMessage12.getMessage().equals("Preference product statement is valid"))) {
                    return responseMessage12;
                }
                String[] parts = productStatement.split(";");
                String productName = parts[0];
                String productPreferred = parts[1];

                ResponseMessage responseMessage13 = verify("productName", productName);

                if (!(responseMessage13.getMessage().equals("Preference product name is valid"))) {
                    return responseMessage13;
                }

                ResponseMessage responseMessage14 = verify("productPreferred", productPreferred);

                if (!(responseMessage14.getMessage().equals("Preference product preferred is valid"))) {
                    return responseMessage14;
                }

                Product product = productRepo.findByProductName(productName);
                DietaryPreferencesProduct dietaryPreferencesProduct = new DietaryPreferencesProduct();
                dietaryPreferencesProduct.setProduct(product);
                dietaryPreferencesProduct.setProductPreferred(productPreferred.equalsIgnoreCase("true"));
                dietaryPreferencesProduct.setDietaryPreferences(updatedDietaryPreference);
                products.add(dietaryPreferencesProduct);
            }

            for (DietaryPreferencesProduct preferencesProduct: currentPreferredProducts) {
                System.out.println(preferencesProduct.getProduct().getProductName());
                preferencesProduct.removeDietaryPreference();
                dietaryPreferencesProductsRepo.delete(preferencesProduct);
            }

            for (DietaryPreferencesProduct preferencesProduct: products) {
                dietaryPreferencesProductsRepo.save(preferencesProduct);
            }

            updatedDietaryPreference.setProducts(products);

            List<String> preferredRecipes = dietaryPreferencesRequest.getRecipes();
            List<DietaryPreferencesRecipe> currentPreferredRecipes = new ArrayList<>();
            currentPreferredRecipes.addAll(updatedDietaryPreference.getRecipes());
            Set<DietaryPreferencesRecipe> recipes = new HashSet<>();

            ResponseMessage responseMessage15 = verify("recipes", preferredRecipes);

            if (!(responseMessage15.getMessage().equals("Preference recipes are valid"))) {
                return responseMessage15;
            }

            for (String recipeStatement: preferredRecipes) {

                ResponseMessage responseMessage16 = verify("recipeStatement", recipeStatement);

                if (!(responseMessage16.getMessage().equals("Preference recipe statement is valid"))) {
                    return responseMessage16;
                }
                String[] parts = recipeStatement.split(";");
                String recipeName = parts[0];
                String recipePreferred = parts[1];

                ResponseMessage responseMessage17 = verify("recipeName", recipeName);

                if (!(responseMessage17.getMessage().equals("Preference recipe name is valid"))) {
                    return responseMessage17;
                }

                ResponseMessage responseMessage18 = verify("recipePreferred", recipePreferred);

                if (!(responseMessage18.getMessage().equals("Preference recipe preferred is valid"))) {
                    return responseMessage18;
                }

                Recipes recipe = recipeRepo.findByRecipeName(recipeName);
                DietaryPreferencesRecipe dietaryPreferencesRecipe = new DietaryPreferencesRecipe();
                dietaryPreferencesRecipe.setRecipe(recipe);
                dietaryPreferencesRecipe.setRecipePreferred(recipePreferred.equalsIgnoreCase("true"));
                dietaryPreferencesRecipe.setDietaryPreferences(updatedDietaryPreference);
                recipes.add(dietaryPreferencesRecipe);
            }

            for (DietaryPreferencesRecipe preferencesRecipe: currentPreferredRecipes) {
                System.out.println(preferencesRecipe.getRecipe().getRecipeName());
                preferencesRecipe.removeDietaryPreference();
                dietaryPreferencesRecipesRepo.delete(preferencesRecipe);
            }

            for (DietaryPreferencesRecipe preferencesRecipe: recipes) {
                dietaryPreferencesRecipesRepo.save(preferencesRecipe);
            }

            updatedDietaryPreference.setRecipes(recipes);

            dietaryPreferencesRepo.save(updatedDietaryPreference);

            return new ResponseMessage("Dietary Preferences " + dietaryPreferenceId + " has been updated");
        }
        return new ResponseMessage("Dietary Preferences " + dietaryPreferenceId + " has not been found");
    }


    @Override
    public ResponseMessage removeDietaryPreferences(Long dietaryPreferenceId) {

        Optional<DietaryPreferences> dietaryPreference = dietaryPreferencesRepo.findById(dietaryPreferenceId);
        if (dietaryPreference.isPresent()) {
            DietaryPreferences removedDietaryPreference = dietaryPreference.get();
            dietaryPreferencesRepo.delete(removedDietaryPreference);
            return new ResponseMessage("Dietary Preference " + removedDietaryPreference.getDietaryPreferenceId() + " has been removed");
        }
        return new ResponseMessage("Dietary Preferences id " + dietaryPreferenceId + " has not been found");
    }

}
