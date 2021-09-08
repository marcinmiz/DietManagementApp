package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.DietaryPreferences;
import agh.edu.pl.diet.entities.DietaryPreferencesNutrient;
import agh.edu.pl.diet.entities.Nutrient;
import agh.edu.pl.diet.payloads.request.DietaryPreferencesRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.*;
import agh.edu.pl.diet.services.DietaryPreferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DietaryPreferencesServiceImpl implements DietaryPreferencesService {

//    @Autowired
//    private ProductRepo productRepo;
    @Autowired
    private DietTypeRepo dietTypeRepo;
    @Autowired
    private DietaryPreferencesRepo dietaryPreferencesRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private NutrientRepo nutrientRepo;

    private ResponseMessage verify(String mode, String type, Object item) {
        switch (type) {
            case "id":
                Long id = Long.parseLong(item.toString());
                if (id == null) {
                    return new ResponseMessage("Dietary Preferences name has to be given");
                } else if (id.toString().length() < 2 || id.toString().length() > 40) {
                    return new ResponseMessage("Dietary Preferences name has to have min 2 and max 40 characters");
                } else if (!(id.toString().matches("^[a-zA-Z ]+$"))) {
                    return new ResponseMessage("Dietary Preferences name has to contain only letters and spaces");
                } else if (!mode.equals("update") && dietaryPreferencesRepo.findByDietaryPreferenceId(id) != null) {
                    return new ResponseMessage("Dietary Preferences with this name exists yet");
                } else {
                    return new ResponseMessage("Dietary Preferences name is valid");
                }
            case "totalCalories":
                if (item == null) {
                    return new ResponseMessage("Dietary Preferences calories has to be given");
                }

                Integer totalCalories = Integer.parseInt(item.toString());

                if (totalCalories.toString().length() < 1 || totalCalories.toString().length() > 10) {
                    return new ResponseMessage("Dietary Preferences calories has to have min 1 and max 10 characters");
                } else if (!(totalCalories.toString().matches("^0$") || totalCalories.toString().matches("^(-)?[1-9]\\d*$"))) {
                    return new ResponseMessage("Dietary Preferences calories has to contain only digits");
                } else if (totalCalories < 0) {
                    return new ResponseMessage("Dietary Preferences calories has to be greater or equal 0");
                } else {
                    return new ResponseMessage("Dietary Preferences calories are valid");
                }
            case "dietType":
                String dietTypeName = String.valueOf(item);
                if (dietTypeName == null) {
                    return new ResponseMessage("Diet type has to be given");
                } else if (dietTypeName.equals("")) {
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

                if (caloriesPerMeal.toString().length() < 1 || caloriesPerMeal.toString().length() > 10) {
                    return new ResponseMessage("Calories per meal has to have min 1 and max 10 characters");
                } else if (!(caloriesPerMeal.toString().matches("^0$") || caloriesPerMeal.toString().matches("^(-)?[1-9]\\d*$"))) {
                    return new ResponseMessage("Calories per meal has to contain only digits");
                } else if (caloriesPerMeal < 0) {
                    return new ResponseMessage("Calories per meal has to be greater or equal 0");
                } else {
                    return new ResponseMessage("Calories per meal are valid");
                }
            case "mealsQuantity":
                if (item == null) {
                    return new ResponseMessage("Meals quantity has to be given");
                }

                Integer mealsQuantity = Integer.parseInt(item.toString());

                if (mealsQuantity.toString().length() < 1 || mealsQuantity.toString().length() > 10) {
                    return new ResponseMessage("Meals quantity has to have min 1 and max 10 characters");
                } else if (!(mealsQuantity.toString().matches("^0$") || mealsQuantity.toString().matches("^(-)?[1-9]\\d*$"))) {
                    return new ResponseMessage("Meals quantity has to contain only digits");
                } else if (mealsQuantity < 0) {
                    return new ResponseMessage("Meals quantity has to be greater or equal 0");
                } else {
                    return new ResponseMessage("Meals quantity are valid");
                }
            case "targetWeight":
                if (item == null) {
                    return new ResponseMessage("Target weight has to be given");
                }

                Integer targetWeight = Integer.parseInt(item.toString());

                if (targetWeight.toString().length() < 1 || targetWeight.toString().length() > 10) {
                    return new ResponseMessage("Target weight has to have min 1 and max 10 characters");
                } else if (!(targetWeight.toString().matches("^0$") || targetWeight.toString().matches("^(-)?[1-9]\\d*$"))) {
                    return new ResponseMessage("Target weight has to contain only digits");
                } else if (targetWeight < 0) {
                    return new ResponseMessage("Target weight has to be greater or equal 0");
                } else {
                    return new ResponseMessage("Target weight are valid");
                }
            case "list":
                List<String> nutrients = (List<String>) item;
                if (nutrients == null) {
                    return new ResponseMessage("Product nutrients has to be given");
                } else if (nutrients.isEmpty()) {
                    return new ResponseMessage("At least 1 product nutrient is required");
                } else {
                    return new ResponseMessage("Product nutrients are valid");
                }
            case "nutrientStatement":
                String nutrientStatement = String.valueOf(item);
                if (nutrientStatement.equals("")) {
                    return new ResponseMessage("Product nutrient has to be defined");
                } else if (!(nutrientStatement.matches("^[a-zA-Z]+;0$") || nutrientStatement.matches("^[a-zA-Z]+;(-)?[1-9]\\d*$"))) {
                    return new ResponseMessage("Product nutrient has to match format \"productName;productAmount\"");
                } else {
                    return new ResponseMessage("Product nutrient statement is valid");
                }
            case "nutrientName":
                String nutrientName = String.valueOf(item);

                if (nutrientRepo.findByNutrientName(nutrientName) == null) {
                    return new ResponseMessage("Product nutrient name has to be proper");
                } else {
                    return new ResponseMessage("Product nutrient name is valid");
                }
            case "nutrientAmount":
                Double nutrientAmount = Double.valueOf(item.toString());

                if (nutrientAmount.toString().length() < 1 || nutrientAmount.toString().length() > 20) {
                    return new ResponseMessage("Product nutrient amount has to have min 1 and max 20 characters");
                } else if (nutrientAmount < 0) {
                    return new ResponseMessage("Product nutrient amount has to be greater or equal 0");
                } else {
                    return new ResponseMessage("Product nutrient amount is valid");
                }

        }

        return new ResponseMessage("Invalid type");
    }

    @Override
    public DietaryPreferences getDietaryPreferences(Long dietaryPreferencesId) {
        DietaryPreferences dietaryPreferences = dietaryPreferencesRepo.findById(dietaryPreferencesId).get();
        return dietaryPreferences;
    }

    @Override
    public ResponseMessage addNewDietaryPreferences(DietaryPreferencesRequest dietaryPreferencesRequest) {
        DietaryPreferences dietaryPreferences = new DietaryPreferences();
        Long dietaryPreferenceId = dietaryPreferencesRequest.getDietaryPreferenceId();

        ResponseMessage responseMessage = verify("add", "id", dietaryPreferencesRequest);
        if (responseMessage.getMessage().equals("DietaryPreferences id is valid")){
            dietaryPreferences.setDietaryPreferenceId(dietaryPreferenceId);
        } else {
            return responseMessage;
        }

        Integer totalCalories = dietaryPreferencesRequest.getTotalCalories();

        ResponseMessage responseMessage2 = verify("add","totalCalories", totalCalories);

        if (responseMessage2.getMessage().equals("Product calories are valid")) {
            dietaryPreferences.setTotalCalories(totalCalories);
        } else {
            return responseMessage2;
        }

        String dietTypeName = dietaryPreferencesRequest.getDietType();

        ResponseMessage responseMessage3 = verify("add", "dietType", dietTypeName);

        if (responseMessage3.getMessage().equals("Diet type is valid")) {
            dietaryPreferences.setDietType(dietTypeRepo.findByDietTypeName(dietTypeName));
        } else {
            return responseMessage3;
        }

        Integer caloriesPerMeal = dietaryPreferencesRequest.getCaloriesPerMeal();

        ResponseMessage responseMessage4 = verify("add","caloriesPerMeal", caloriesPerMeal);

        if (responseMessage4.getMessage().equals("Calories per meal are valid")) {
            dietaryPreferences.setCaloriesPerMeal(caloriesPerMeal);
        } else {
            return responseMessage4;
        }

        Integer mealsQuantity = dietaryPreferencesRequest.getMealsQuantity();

        ResponseMessage responseMessage5 = verify("add","mealsQuantity", mealsQuantity);

        if (responseMessage5.getMessage().equals("Meals quantity are valid")) {
            dietaryPreferences.setMealsQuantity(mealsQuantity);
        } else {
            return responseMessage5;
        }

        Integer targetWeight = dietaryPreferencesRequest.getTargetWeight();

        ResponseMessage responseMessage6 = verify("add","targetWeight", targetWeight);

        if (responseMessage6.getMessage().equals("Target weight are valid")) {
            dietaryPreferences.setTargetWeight(targetWeight);
        } else {
            return responseMessage6;
        }

        List<String> nutrients = dietaryPreferencesRequest.getNutrients();

        ResponseMessage responseMessage7 = verify("add", "list", nutrients);

        if (!(responseMessage7.getMessage().equals("Product nutrients are valid"))) {
            return responseMessage7;
        }

        for (String nutrientStatement: nutrients) {

            ResponseMessage responseMessage8 = verify("add","nutrientStatement", nutrientStatement);

            if (!(responseMessage8.getMessage().equals("Product nutrient statement is valid"))) {
                return responseMessage8;
            }
            String[] parts = nutrientStatement.split(";");
            String nutrientName = parts[0];
            Double nutrientAmount = Double.valueOf(parts[1]);

            ResponseMessage responseMessage9 = verify("add","nutrientName", nutrientName);

            if (!(responseMessage9.getMessage().equals("Product nutrient name is valid"))) {
                return responseMessage9;
            }

            ResponseMessage responseMessage10 = verify("add","nutrientAmount", nutrientAmount);

            if (!(responseMessage10.getMessage().equals("Product nutrient amount is valid"))) {
                return responseMessage10;
            }

            Nutrient nutrient = nutrientRepo.findByNutrientName(nutrientName);
            DietaryPreferencesNutrient dietaryPreferencesNutrient = new DietaryPreferencesNutrient();
            dietaryPreferencesNutrient.setNutrient(nutrient);
            dietaryPreferencesNutrient.setNutrientAmount(nutrientAmount);
            dietaryPreferencesNutrient.setDietaryPreferences(dietaryPreferences);
            dietaryPreferences.addNutrient(dietaryPreferencesNutrient);
        }

        //change to logged in user id
        dietaryPreferences.setPreferenceOwner(userRepo.findById(256L).get());
        String creationDate = new Date().toInstant().toString();
        dietaryPreferences.setCreationDate(creationDate);

        dietaryPreferencesRepo.save(dietaryPreferences);
//        DietaryPreferences lastAddedDietaryPreferences = getDietaryPreferences().stream().filter(p -> p.getCreationDate().equals(creationDate)).findFirst().orElse(null);
//        if (lastAddedDietaryPreferences != null) {
//            return new ResponseMessage(lastAddedDietaryPreferences.getDietaryPreferenceId() + " Dietary Preference " + dietaryPreferencesName + " has been added successfully");
//        } else {
          return new ResponseMessage("Dietary Preference " + dietaryPreferenceId + " has not been found");
//        }
    }

    @Override
    public ResponseMessage updateDietaryPreferences(Long dietaryPreferencesId, DietaryPreferencesRequest dietaryPreferencesRequest) {

        Long dietaryPreferenceId = null;
        Optional<DietaryPreferences> dietaryPreferences = dietaryPreferencesRepo.findById(dietaryPreferenceId);
        if (dietaryPreferences.isPresent()) {
            DietaryPreferences updatedDietaryPreferences = dietaryPreferences.get();
            dietaryPreferenceId = dietaryPreferencesRequest.getDietaryPreferenceId();

            ResponseMessage responseMessage = verify("update", "name", dietaryPreferenceId);
            if (responseMessage.getMessage().equals("Dietary Preferences name is valid")){
                updatedDietaryPreferences.setDietaryPreferenceId(dietaryPreferenceId);
            } else {
                return responseMessage;
            }

            Integer totalCalories = dietaryPreferencesRequest.getTotalCalories();

            ResponseMessage responseMessage2 = verify("update", "calories", totalCalories);

            if (responseMessage2.getMessage().equals("Product calories are valid")) {
                updatedDietaryPreferences.setTotalCalories(totalCalories);
            } else {
                return responseMessage2;
            }

            String dietTypeName = dietaryPreferencesRequest.getDietType();

            ResponseMessage responseMessage3 = verify("update", "dietType", dietTypeName);

            if (responseMessage3.getMessage().equals("Diet type is valid")) {
                updatedDietaryPreferences.setDietType(dietTypeRepo.findByDietTypeName(dietTypeName));
            } else {
                return responseMessage3;
            }

            Integer caloriesPerMeal = dietaryPreferencesRequest.getCaloriesPerMeal();

            ResponseMessage responseMessage4 = verify("update","caloriesPerMeal", caloriesPerMeal);

            if (responseMessage4.getMessage().equals("Calories per meal are valid")) {
                updatedDietaryPreferences.setCaloriesPerMeal(caloriesPerMeal);
            } else {
                return responseMessage4;
            }

            Integer mealsQuantity = dietaryPreferencesRequest.getMealsQuantity();

            ResponseMessage responseMessage5 = verify("add","mealsQuantity", mealsQuantity);

            if (responseMessage5.getMessage().equals("Meals quantity are valid")) {
                updatedDietaryPreferences.setMealsQuantity(mealsQuantity);
            } else {
                return responseMessage5;
            }

            Integer targetWeight = dietaryPreferencesRequest.getTargetWeight();

            ResponseMessage responseMessage6 = verify("add","targetWeight", targetWeight);

            if (responseMessage6.getMessage().equals("Target weight are valid")) {
                updatedDietaryPreferences.setTargetWeight(targetWeight);
            } else {
                return responseMessage6;
            }

            List<String> nutrients = dietaryPreferencesRequest.getNutrients();

            ResponseMessage responseMessage7 = verify("update", "list", nutrients);

            if (!(responseMessage7.getMessage().equals("Product nutrients are valid"))) {
                return responseMessage7;
            }

            for (String nutrientStatement: nutrients) {

                ResponseMessage responseMessage8 = verify("update", "nutrientStatement", nutrientStatement);

                if (!(responseMessage8.getMessage().equals("Product nutrient statement is valid"))) {
                    return responseMessage8;
                }
                String[] parts = nutrientStatement.split(";");
                String nutrientName = parts[0];
                Double nutrientAmount = Double.valueOf(parts[1]);

                ResponseMessage responseMessage9 = verify("update", "nutrientName", nutrientName);

                if (!(responseMessage9.getMessage().equals("Product nutrient name is valid"))) {
                    return responseMessage9;
                }

                ResponseMessage responseMessage10 = verify("update", "nutrientAmount", nutrientAmount);

                if (!(responseMessage10.getMessage().equals("Product nutrient amount is valid"))) {
                    return responseMessage10;
                }

                DietaryPreferencesNutrient dietaryPreferencesNutrient = updatedDietaryPreferences.getNutrients().stream().filter(pn -> pn.getNutrient().getNutrientName().equals(nutrientName)).findFirst().orElse(null);

                if (dietaryPreferencesNutrient != null) {
                    dietaryPreferencesNutrient.setNutrientAmount(nutrientAmount);
                } else {
                    return new ResponseMessage( "Nutrient " + nutrientName + " belonging to product " + dietaryPreferenceId + " has not been found");
                }
            }
            dietaryPreferencesRepo.save(updatedDietaryPreferences);

            return new ResponseMessage("Dietary Preferences " + dietaryPreferenceId + " has been updated successfully");
        }
        return new ResponseMessage("Dietary Preferences " + dietaryPreferenceId + " has not been found");
    }


    @Override
    public ResponseMessage removeDietaryPreferences(Long dietaryPreferencesId) {

        Optional<DietaryPreferences> dietaryPreferences = dietaryPreferencesRepo.findById(dietaryPreferencesId);
        if (dietaryPreferences.isPresent()) {
            DietaryPreferences removedDietaryPreferences = dietaryPreferences.get();
            dietaryPreferencesRepo.delete(removedDietaryPreferences);
            return new ResponseMessage("Dietary Preferences " + removedDietaryPreferences.getDietaryPreferenceId() + " has been removed successfully");
        }
        return new ResponseMessage("Dietary Preferences id " + dietaryPreferencesId + " has not been found");
    }

//    @Override
//    public void removeOne(Long id) {
//
//    }

}
