package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.Meals;
import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.payloads.request.MealRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.DailyMenuRepo;
import agh.edu.pl.diet.repos.MealRepo;
import agh.edu.pl.diet.repos.RecipeRepo;
import agh.edu.pl.diet.repos.UserRepo;
import agh.edu.pl.diet.services.MealService;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

    @Service
    public class MealServiceImpl implements MealService {

        @Autowired
        private MealRepo mealRepo;
        @Autowired
        private RecipeRepo recipeRepo;
        @Autowired
        private DailyMenuRepo dailyMenuRepo;
        @Autowired
        private UserRepo userRepo;
        @Autowired
        private UserService userService;

        private ResponseMessage verify(String mode, String type, Object item) {
            switch (type) {
                case "name":
                    String name = String.valueOf(item);
                    if (name == null) {
                        return new ResponseMessage("Meal name has to be given");
                    } else if (name.length() < 2 || name.length() > 40) {
                        return new ResponseMessage("Meal name has to have min 2 and max 40 characters");
                    } else if (!(name.matches("^[a-zA-Z ]+$"))) {
                        return new ResponseMessage("Meal name has to contain only letters and spaces");
                    } else if (!mode.equals("update") && mealRepo.findByMealsName(name) != null) {
                        return new ResponseMessage("Meal with this name exists yet");
                    } else {
                        return new ResponseMessage("Meal name is valid");
                    }

                case "mealHourTime":
                    String mealHourTime = String.valueOf(item);
                    if (mealHourTime == null) {
                        return new ResponseMessage("Meal Hour Time name has to be given");
                    } else if (mealHourTime.length() < 2 || mealHourTime.length() > 40) {
                        return new ResponseMessage("Meal Hour Time name has to have min 2 and max 40 characters");
                    } else if (!(mealHourTime.matches("^[a-zA-Z ]+$"))) {
                        return new ResponseMessage("Meal Hour Time name has to contain only letters and spaces");
                    } else if (!mode.equals("update") && mealRepo.findByMealsName(mealHourTime) != null) {
                        return new ResponseMessage("Meal Hour Time with this name exists yet");
                    } else {
                        return new ResponseMessage("Meal Hour Time name is valid");
                    }

                case "list":
                    List<String> dailyMenu = (List<String>) item;
                    if (dailyMenu == null) {
                        return new ResponseMessage("Daily Menu has to be given");
                    } else if (dailyMenu.isEmpty()) {
                        return new ResponseMessage("At least 1 daily menu is required");
                    } else {
                        return new ResponseMessage("Daily Menu are valid");
                    }
                case "dailyMenuStatement":
                    String dailyMenuStatement = String.valueOf(item);
                    if (dailyMenuStatement.equals("")) {
                        return new ResponseMessage("Daily Menu has to be defined");
                    } else if (!(dailyMenuStatement.matches("^[a-zA-Z]+;0$") || dailyMenuStatement.matches("^[a-zA-Z]+;(-)?[1-9]\\d*$"))) {
                        return new ResponseMessage("Daily Menu has to match format \"productName;productAmount\"");
                    } else {
                        return new ResponseMessage("Daily Menu statement is valid");
                    }

                case "recipeStatement":
                    String recipeStatement = String.valueOf(item);
                    if (recipeStatement.equals("")) {
                        return new ResponseMessage("Recipe has to be defined");
                    } else if (!(recipeStatement.matches("^[a-zA-Z]+;0$") || recipeStatement.matches("^[a-zA-Z]+;(-)?[1-9]\\d*$"))) {
                        return new ResponseMessage("Recipe has to match format \"productName;productAmount\"");
                    } else {
                        return new ResponseMessage("Recipe statement is valid");
                    }

            }

            return new ResponseMessage("Invalid type");
        }

        @Override
        public List<Meals> getAllMeals() {
            List<Meals> list = new ArrayList<>();
            mealRepo.findAll().forEach(list::add);
            return list;
        }

        @Override
        public Meals getMeal(Long dailyMenuId) {
            Meals meals = mealRepo.findById(dailyMenuId).get();
            return meals;
        }

        @Override
        public ResponseMessage addNewMeal(MealRequest mealRequest) {
            Meals meal = new Meals();
            String mealName = mealRequest.getMealName();

            ResponseMessage responseMessage = verify("add", "name", mealName);
            if (responseMessage.getMessage().equals("Meal name is valid")){
                meal.setMealsName(mealName);
            } else {
                return responseMessage;
            }

            List<String> dailyMenu = mealRequest.getDailyMenu();

            ResponseMessage responseMessage4 = verify("add", "list", dailyMenu);

            if (!(responseMessage4.getMessage().equals("Daily Menu are valid"))) {
                return responseMessage4;
            }

            for (String dailyMenuStatement: dailyMenu) {

                ResponseMessage responseMessage5 = verify("add", "dailyMenuStatement", dailyMenuStatement);

                if (!(responseMessage5.getMessage().equals("Daily menu statement is valid"))) {
                    return responseMessage5;
                }
                String[] parts3 = dailyMenuStatement.split(";");
                String dailyMenuName = parts3[0];
//                Double nutrientAmount = Double.valueOf(parts3[1]);

                ResponseMessage responseMessage6 = verify("add", "nutrientName", dailyMenuName);

                if (!(responseMessage6.getMessage().equals("Daily menu name is valid"))) {
                    return responseMessage6;
                }

//                ResponseMessage responseMessage7 = verify("add","nutrientAmount", nutrientAmount);
//
//                if (!(responseMessage7.getMessage().equals("Daily menu amount is valid"))) {
//                    return responseMessage7;
//                }
            }

            List<String> recipe = mealRequest.getRecipe();

            ResponseMessage responseMessage8 = verify("add", "list", recipe);

            if (!(responseMessage8.getMessage().equals("Recipe are valid"))) {
                return responseMessage8;
            }

            for (String recipeStatement: dailyMenu) {

                ResponseMessage responseMessage9 = verify("add","recipeStatement", recipeStatement);

                if (!(responseMessage9.getMessage().equals("Recipe statement is valid"))) {
                    return responseMessage9;
                }
                String[] parts4 = recipeStatement.split(";");
                String recipeName = parts4[0];
//            Double recipeAmount = Double.valueOf(parts4[1]);

                ResponseMessage responseMessage10 = verify("add","nutrientName", recipeName);

                if (!(responseMessage10.getMessage().equals("Recipe name is valid"))) {
                    return responseMessage10;
                }


//                Nutrient nutrient = nutrientRepo.findByNutrientName(nutrientName);
//                ProductNutrient productNutrient = new ProductNutrient();
//                productNutrient.setNutrient(nutrient);
//                productNutrient.setNutrientAmount(nutrientAmount);
//                productNutrient.setProduct(meal);
//                meal.addNutrient(productNutrient);
            }

            //change to logged in user id
//            User productOwner = userService.getLoggedUser();
//            if (productOwner == null) {
//                return new ResponseMessage("Meal " + mealName + " owner has not been found");
//            }
//            meal.setOwner(productOwner);
//            String creationDate = new Date().toInstant().toString();
//            meal.setCreationDate(creationDate);

            mealRepo.save(meal);
            Meals lastAddedMeal = getAllMeals().stream().findFirst().orElse(null);
            if (lastAddedMeal != null) {
                return new ResponseMessage(lastAddedMeal.getMealId() + " Meal " + mealName + " has been added successfully");
            } else {
                return new ResponseMessage("Meal " + mealName + " has not been found");
            }
        }

        @Override
        public ResponseMessage updateMeals(Long mealId, MealRequest mealRequest) {

            String mealName = "";
            Optional<Meals> meal = mealRepo.findById(mealId);
            if (meal.isPresent()) {
                Meals updatedMeal = meal.get();

                User currentLoggedUser = userService.getLoggedUser();

                if (currentLoggedUser == null) {
                    return new ResponseMessage("Current logged user has not been found");
                }

                mealName = mealRequest.getMealName();

                ResponseMessage responseMessage = verify("update", "name", mealName);
                if (responseMessage.getMessage().equals("Product name is valid")) {
                    updatedMeal.setMealsName(mealName);
                } else {
                    return responseMessage;
                }

                List<String> dailyMenu = mealRequest.getDailyMenu();

                ResponseMessage responseMessage4 = verify("update", "list", dailyMenu);

                if (!(responseMessage4.getMessage().equals("Daily Menu are valid"))) {
                    return responseMessage4;
                }

                for (String dailyMenuStatement : dailyMenu) {

                    ResponseMessage responseMessage5 = verify("update", "nutrientStatement", dailyMenuStatement);

                    if (!(responseMessage5.getMessage().equals("Product nutrient statement is valid"))) {
                        return responseMessage5;
                    }
                    String[] parts = dailyMenuStatement.split(";");
                    String dailyMenuName = parts[0];
                    Integer mealsQuantity = Integer.valueOf(parts[1]);

                    ResponseMessage responseMessage6 = verify("update", "dailyMenuName", dailyMenuName);

                    if (!(responseMessage6.getMessage().equals("Daily Menu Name name is valid"))) {
                        return responseMessage6;
                    }

                    ResponseMessage responseMessage7 = verify("update", "mealsQuantity", mealsQuantity);

                    if (!(responseMessage7.getMessage().equals("Meals Quantity is valid"))) {
                        return responseMessage7;
                    }
                }

                    List<String> recipe = mealRequest.getRecipe();

                    ResponseMessage responseMessage8 = verify("update", "list", recipe);

                    if (!(responseMessage8.getMessage().equals("Recipe are valid"))) {
                        return responseMessage8;
                    }

                    for (String recipeStatement : recipe) {

                        ResponseMessage responseMessage9 = verify("update", "recipeStatement", recipeStatement);

                        if (!(responseMessage9.getMessage().equals("Recipe statement is valid"))) {
                            return responseMessage9;
                        }
                        String[] parts2 = recipeStatement.split(";");
                        String recipeName = parts2[0];
//                        Double nutrientAmount = Double.valueOf(parts2[1]);

                        ResponseMessage responseMessage10 = verify("update", "recipeName", recipeName);

                        if (!(responseMessage10.getMessage().equals("Recipe name is valid"))) {
                            return responseMessage10;
                        }

//                        ResponseMessage responseMessage11 = verify("update", "nutrientAmount", nutrientAmount);
//
//                        if (!(responseMessage11.getMessage().equals("Product nutrient amount is valid"))) {
//                            return responseMessage11;
//                        }
                    }

                    mealRepo.save(updatedMeal);

                    return new ResponseMessage("Meal " + mealName + " has been updated successfully");
                }
                return new ResponseMessage("Meal " + mealName + " has not been found");
            }

        @Override
        public ResponseMessage removeMeal(Long mealId) {

                    Optional<Meals> meal = mealRepo.findById(mealId);
                    if (meal.isPresent()) {
                        Meals removedMeal = meal.get();

                        User currentLoggedUser2 = userService.getLoggedUser();

                        if (currentLoggedUser2 == null) {
                            return new ResponseMessage("Current logged user has not been found");
                        }

                        mealRepo.delete(removedMeal);
                        return new ResponseMessage("Meal " + removedMeal.getMealsName() + " has been removed successfully");
                    }
                    return new ResponseMessage("Meal id " + mealId + " has not been found");
                }

            }
