package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.DailyMenu;
import agh.edu.pl.diet.payloads.request.DailyMenuRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.DailyMenuRepo;
import agh.edu.pl.diet.services.DailyMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DailyMenuServiceImpl implements DailyMenuService {

    @Autowired
    private DailyMenuRepo dailyMenuRepo;
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
    public DailyMenu getDailyMenu(Long dailyMenuId) {
        DailyMenu dailyMenu = dailyMenuRepo.findById(dailyMenuId).get();
        return dailyMenu;
    }

    @Override
    public ResponseMessage addNewDailyMenu(DailyMenuRequest dailyMenuRequest) {
        DailyMenu dailyMenu = new DailyMenu();
        String dailyMenuName = dailyMenuRequest.getDailyMenuName();

        ResponseMessage responseMessage = verify("add", "name", dailyMenuName);
        if (responseMessage.getMessage().equals("DailyMenu name is valid")){
            dailyMenu.setDailyMenuName(dailyMenuName);
        } else {
            return responseMessage;
        }

        String dailyMenuDate = dailyMenuRequest.getDailyMenuDate();

        ResponseMessage responseMessage2 = verify("add","Daily Menu",  dailyMenuDate);

        if (responseMessage2.getMessage().equals("Product calories are valid")) {
            dailyMenu.getDailyMenuDate();
        } else {
            return responseMessage2;
        }

        Integer mealsQuantity = dailyMenuRequest.getMealsQuantity();

        ResponseMessage responseMessage3 = verify("add", "mealsQuantity", mealsQuantity);

        if (responseMessage3.getMessage().equals("Product category is valid")) {
            dailyMenu.getMealsQuantity();
        } else {
            return responseMessage3;
        }

        List<String> meals = dailyMenuRequest.getMeals();

        ResponseMessage responseMessage4 = verify("add", "list", meals);

        if (!(responseMessage4.getMessage().equals("Meals are valid"))) {
            return responseMessage4;
        }

        for (String mealsStatement: meals) {

            ResponseMessage responseMessage5 = verify("add","mealsStatement", mealsStatement);

            if (!(responseMessage5.getMessage().equals("Meals statement is valid"))) {
                return responseMessage5;
            }
            String[] parts = mealsStatement.split(";");
            String mealsName = parts[0];
//            Double nutrientAmount = Double.valueOf(parts[1]);

            ResponseMessage responseMessage6 = verify("add","mealsName", mealsName);

            if (!(responseMessage6.getMessage().equals("Meals name is valid"))) {
                return responseMessage6;
            }

//            ResponseMessage responseMessage7 = verify("add","nutrientAmount", nutrientAmount);
//
//            if (!(responseMessage7.getMessage().equals("Product nutrient amount is valid"))) {
//                return responseMessage7;
//            }

//            Nutrient nutrient = nutrientRepo.findByNutrientName(nutrientName);
//            ProductNutrient productNutrient = new ProductNutrient();
//            productNutrient.setNutrient(nutrient);
//            productNutrient.setNutrientAmount(nutrientAmount);
//            productNutrient.setProduct(product);
//            product.addNutrient(productNutrient);
        }

//        //change to logged in user id
//        product.setOwner(userRepo.findById(262L).get());
        String creationDate = new Date().toInstant().toString();
        dailyMenu.setCreationDate(creationDate);

        dailyMenuRepo.save(dailyMenu);
//        DailyMenu lastAddedDailyMenu = getDailyMenu().stream().filter(p -> p.getCreationDate().equals(creationDate)).findFirst().orElse(null);
//        if (lastAddedDailyMenu != null) {
//            return new ResponseMessage(lastAddedDailyMenu.getDailyMenuId() + " Daily Menu " + dailyMenuName + " has been added successfully");
//        } else {
            return new ResponseMessage("Daily Menu " + dailyMenuName + " has not been found");
        }

    @Override
    public ResponseMessage updateDailyMenu(Long dailyMenuId, DailyMenuRequest dailyMenuRequest) {

        String dailyMenuName = "";
        Optional<DailyMenu> dailyMenu = dailyMenuRepo.findById(dailyMenuId);
        if (dailyMenu.isPresent()) {
            DailyMenu updatedDailyMenu = dailyMenu.get();
            dailyMenuName = dailyMenuRequest.getDailyMenuName();

            ResponseMessage responseMessage = verify("update", "name", dailyMenuName);
            if (responseMessage.getMessage().equals("Daily menu name is valid")){
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

            for (String mealsStatement: meals) {

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
