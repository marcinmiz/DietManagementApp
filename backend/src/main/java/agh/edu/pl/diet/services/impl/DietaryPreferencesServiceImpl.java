package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.DietaryPreferences;
import agh.edu.pl.diet.entities.Nutrient;
import agh.edu.pl.diet.entities.Product;
import agh.edu.pl.diet.entities.ProductNutrient;
import agh.edu.pl.diet.payloads.request.DietaryPreferencesRequest;
import agh.edu.pl.diet.payloads.request.ProductRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.DietTypeRepo;
import agh.edu.pl.diet.repos.DietaryPreferencesRepo;
import agh.edu.pl.diet.repos.ProductRepo;
import agh.edu.pl.diet.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DietaryPreferencesServiceImpl {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private DietTypeRepo dietTypeRepo;
    @Autowired
    private DietaryPreferencesRepo dietaryPreferencesRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private Nutrient nutrientRepo;

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
        String dietaryPreferencesName = DietaryPreferencesRequest.getDietaryPreferencesName();

        ResponseMessage responseMessage = verify("add", "name", dietaryPreferencesRequestName);
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
            ProductNutrient productNutrient = new ProductNutrient();
            productNutrient.setNutrient(nutrient);
            productNutrient.setNutrientAmount(nutrientAmount);
            productNutrient.setProduct(dietaryPreferences);
            dietaryPreferences.addNutrient(productNutrient);
        }

        //change to logged in user id
        dietaryPreferences.setOwner(userRepo.findById(1L).get());
        String creationDate = new Date().toInstant().toString();
        dietaryPreferences.setCreationDate(creationDate);

        productRepo.save(dietaryPreferences);
        Product lastAddedProduct = getAllProducts().stream().filter(p -> p.getCreationDate().equals(creationDate)).findFirst().orElse(null);
        if (lastAddedProduct != null) {
            return new ResponseMessage(lastAddedProduct.getProductId() + " Product " + productName + " has been added successfully");
        } else {
            return new ResponseMessage("Product " + dietaryPreferencesName + " has not been found");
        }
    }

    @Override
    public ResponseMessage updateProduct(Long productId, ProductRequest productRequest) {

        String productName = "";
        Optional<Product> product = productRepo.findById(productId);
        if (product.isPresent()) {
            Product updatedProduct = product.get();
            productName = productRequest.getProductName();

            ResponseMessage responseMessage = verify("update", "name", productName);
            if (responseMessage.getMessage().equals("Product name is valid")){
                updatedProduct.setProductName(productName);
            } else {
                return responseMessage;
            }

            Integer calories = productRequest.getCalories();

            ResponseMessage responseMessage2 = verify("update", "calories", calories);

            if (responseMessage2.getMessage().equals("Product calories are valid")) {
                updatedProduct.setCalories(calories);
            } else {
                return responseMessage2;
            }

            String categoryName = productRequest.getCategory();

            ResponseMessage responseMessage3 = verify("update", "category", categoryName);

            if (responseMessage3.getMessage().equals("Product category is valid")) {
                updatedProduct.setCategory(categoryRepo.findByCategoryName(categoryName));
            } else {
                return responseMessage3;
            }

            List<String> nutrients = productRequest.getNutrients();

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

                ProductNutrient productNutrient = updatedProduct.getNutrients().stream().filter(pn -> pn.getNutrient().getNutrientName().equals(nutrientName)).findFirst().orElse(null);

                if (productNutrient != null) {
                    productNutrient.setNutrientAmount(nutrientAmount);
                } else {
                    return new ResponseMessage( "Nutrient " + nutrientName + " belonging to product " + productName + " has not been found");
                }
            }
            productRepo.save(updatedProduct);

            return new ResponseMessage("Product " + productName + " has been updated successfully");
        }
        return new ResponseMessage("Product " + productName + " has not been found");
    }

    @Override
    public ResponseMessage removeProduct(Long productId) {

        Optional<Product> product = productRepo.findById(productId);
        if (product.isPresent()) {
            Product removedProduct = product.get();
            productRepo.delete(removedProduct);
            return new ResponseMessage("Product " + removedProduct.getProductName() + " has been removed successfully");
        }
        return new ResponseMessage("Product id " + productId + " has not been found");
    }

}
