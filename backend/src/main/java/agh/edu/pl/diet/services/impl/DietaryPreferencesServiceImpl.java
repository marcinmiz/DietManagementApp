package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.DietaryPreferences;
import agh.edu.pl.diet.entities.DietaryPreferencesNutrient;
import agh.edu.pl.diet.entities.Nutrient;
import agh.edu.pl.diet.payloads.request.DietaryPreferencesRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.*;
import agh.edu.pl.diet.services.DietaryPreferencesService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DietaryPreferencesServiceImpl implements DietaryPreferencesService {

    @Autowired
    private ProductRepo productRepo;
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
            case "name":
                String name = String.valueOf(item);
                if (name == null) {
                    return new ResponseMessage("Dietary Preferences name has to be given");
                } else if (name.length() < 2 || name.length() > 40) {
                    return new ResponseMessage("Dietary Preferences name has to have min 2 and max 40 characters");
                } else if (!(name.matches("^[a-zA-Z ]+$"))) {
                    return new ResponseMessage("Dietary Preferences name has to contain only letters and spaces");
                } else if (!mode.equals("update") && dietaryPreferencesRepo.findByDietaryPreferencesName(name) != null) {
                    return new ResponseMessage("Dietary Preferences with this name exists yet");
                } else {
                    return new ResponseMessage("Dietary Preferences name is valid");
                }
            case "calories":
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
        String dietaryPreferencesName = dietaryPreferencesRequest.getDietaryPreferencesName();

        ResponseMessage responseMessage = verify("add", "name", dietaryPreferencesRequest);
        if (responseMessage.getMessage().equals("DietaryPreferences name is valid")){
            dietaryPreferences.setDietaryPreferencesName(dietaryPreferencesName);
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

        List<String> nutrients = dietaryPreferencesRequest.getNutrients();

        ResponseMessage responseMessage4 = verify("add", "list", nutrients);

        if (!(responseMessage4.getMessage().equals("Product nutrients are valid"))) {
            return responseMessage4;
        }

        for (String nutrientStatement: nutrients) {

            ResponseMessage responseMessage5 = verify("add","nutrientStatement", nutrientStatement);

            if (!(responseMessage5.getMessage().equals("Product nutrient statement is valid"))) {
                return responseMessage5;
            }
            String[] parts = nutrientStatement.split(";");
            String nutrientName = parts[0];
            Double nutrientAmount = Double.valueOf(parts[1]);

            ResponseMessage responseMessage6 = verify("add","nutrientName", nutrientName);

            if (!(responseMessage6.getMessage().equals("Product nutrient name is valid"))) {
                return responseMessage6;
            }

            ResponseMessage responseMessage7 = verify("add","nutrientAmount", nutrientAmount);

            if (!(responseMessage7.getMessage().equals("Product nutrient amount is valid"))) {
                return responseMessage7;
            }

            Nutrient nutrient = nutrientRepo.findByNutrientName(nutrientName);
            DietaryPreferencesNutrient dietaryPreferencesNutrient = new DietaryPreferencesNutrient();
            dietaryPreferencesNutrient.setNutrient(nutrient);
            dietaryPreferencesNutrient.setNutrientAmount(nutrientAmount);
            dietaryPreferencesNutrient.setDietaryPreferences(dietaryPreferences);
            dietaryPreferences.addNutrient(dietaryPreferencesNutrient);
        }

        //change to logged in user id
        dietaryPreferences.setOwner(userRepo.findById(256L).get());
        String creationDate = new Date().toInstant().toString();
        dietaryPreferences.setCreationDate(creationDate);

        dietaryPreferencesRepo.save(dietaryPreferences);
//        DietaryPreferences lastAddedDietaryPreferences = getDietaryPreferences().stream().filter(p -> p.getCreationDate().equals(creationDate)).findFirst().orElse(null);
//        if (lastAddedDietaryPreferences != null) {
//            return new ResponseMessage(lastAddedDietaryPreferences.getDietaryPreferenceId() + " Dietary Preference " + dietaryPreferencesName + " has been added successfully");
//        } else {
          return new ResponseMessage("Dietary Preference " + dietaryPreferencesName + " has not been found");
//        }
    }

    @Override
    public ResponseMessage updateDietaryPreferences(Long dietaryPreferencesId, DietaryPreferencesRequest dietaryPreferencesRequest) {

        String dietaryPreferencesName = "";
        Optional<DietaryPreferences> dietaryPreferences = dietaryPreferencesRepo.findById(dietaryPreferencesId);
        if (dietaryPreferences.isPresent()) {
            DietaryPreferences updatedDietaryPreferences = dietaryPreferences.get();
            dietaryPreferencesName = dietaryPreferencesRequest.getDietaryPreferencesName();

            ResponseMessage responseMessage = verify("update", "name", dietaryPreferencesName);
            if (responseMessage.getMessage().equals("Dietary Preferences name is valid")){
                updatedDietaryPreferences.setDietaryPreferencesName(dietaryPreferencesName);
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

            List<String> nutrients = dietaryPreferencesRequest.getNutrients();

            ResponseMessage responseMessage4 = verify("update", "list", nutrients);

            if (!(responseMessage4.getMessage().equals("Product nutrients are valid"))) {
                return responseMessage4;
            }

            for (String nutrientStatement: nutrients) {

                ResponseMessage responseMessage5 = verify("update", "nutrientStatement", nutrientStatement);

                if (!(responseMessage5.getMessage().equals("Product nutrient statement is valid"))) {
                    return responseMessage5;
                }
                String[] parts = nutrientStatement.split(";");
                String nutrientName = parts[0];
                Double nutrientAmount = Double.valueOf(parts[1]);

                ResponseMessage responseMessage6 = verify("update", "nutrientName", nutrientName);

                if (!(responseMessage6.getMessage().equals("Product nutrient name is valid"))) {
                    return responseMessage6;
                }

                ResponseMessage responseMessage7 = verify("update", "nutrientAmount", nutrientAmount);

                if (!(responseMessage7.getMessage().equals("Product nutrient amount is valid"))) {
                    return responseMessage7;
                }

                DietaryPreferencesNutrient dietaryPreferencesNutrient = updatedDietaryPreferences.getNutrients().stream().filter(pn -> pn.getNutrient().getNutrientName().equals(nutrientName)).findFirst().orElse(null);

                if (dietaryPreferencesNutrient != null) {
                    dietaryPreferencesNutrient.setNutrientAmount(nutrientAmount);
                } else {
                    return new ResponseMessage( "Nutrient " + nutrientName + " belonging to product " + dietaryPreferencesName + " has not been found");
                }
            }
            dietaryPreferencesRepo.save(updatedDietaryPreferences);

            return new ResponseMessage("Dietary Preferences " + dietaryPreferencesName + " has been updated successfully");
        }
        return new ResponseMessage("Dietary Preferences " + dietaryPreferencesName + " has not been found");
    }


    @Override
    public ResponseMessage removeDietaryPreferences(Long dietaryPreferencesId) {

        Optional<DietaryPreferences> dietaryPreferences = dietaryPreferencesRepo.findById(dietaryPreferencesId);
        if (dietaryPreferences.isPresent()) {
            DietaryPreferences removedDietaryPreferences = dietaryPreferences.get();
            dietaryPreferencesRepo.delete(removedDietaryPreferences);
            return new ResponseMessage("Dietary Preferences " + removedDietaryPreferences.getDietaryPreferencesName() + " has been removed successfully");
        }
        return new ResponseMessage("Dietary Preferences id " + dietaryPreferencesId + " has not been found");
    }

    @Override
    public void removeOne(Long id) {

    }

}