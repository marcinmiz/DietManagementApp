package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.controllers.ImageController;
import agh.edu.pl.diet.entities.*;
import agh.edu.pl.diet.payloads.request.ItemAssessRequest;
import agh.edu.pl.diet.payloads.request.RecipeGetRequest;
import agh.edu.pl.diet.payloads.request.RecipeRequest;
import agh.edu.pl.diet.payloads.request.RecipeSearchRequest;
import agh.edu.pl.diet.payloads.response.*;
import agh.edu.pl.diet.repos.*;
import agh.edu.pl.diet.services.RecipeService;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final Path root = Paths.get("images/recipes");
    private final List<String> recipeProductUnits = Arrays.asList("kg", "dag", "g", "pcs", "ml", "l");

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private RecipeRepo recipeRepo;
    @Autowired
    private RecipeCollectionRepo recipeCollectionRepo;
    @Autowired
    private RecipeCustomerSatisfactionsRepo recipeCustomerSatisfactionsRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;

    private ResponseMessage verify(String type, Object item) {
        switch (type) {
            case "name":

                if (item == null) {
                    return new ResponseMessage("Recipe name has to be given");
                }

                String name = String.valueOf(item);

                if (name.length() < 2 || name.length() > 40) {
                    return new ResponseMessage("Recipe name has to have min 2 and max 40 characters");
                } else if (!(name.matches("^[a-zA-Z ]+$"))) {
                    return new ResponseMessage("Recipe name has to contain only letters and spaces");
                } else {
                    return new ResponseMessage("Recipe name is valid");
                }

            case "recipeProducts":
                List<String> recipeProducts = (List<String>) item;
                if (recipeProducts == null) {
                    return new ResponseMessage("Recipe products has to be given");
                } else if (recipeProducts.isEmpty()) {
                    return new ResponseMessage("At least 1 recipe product is required");
                } else {
                    return new ResponseMessage("Recipe products are valid");
                }

            case "recipeProductStatement":
                String recipeProductStatement = String.valueOf(item);
                if (recipeProductStatement.equals("")) {
                    return new ResponseMessage("Recipe product has to be defined");
                } else if (!(recipeProductStatement.matches("^[a-zA-Z ]+;0(.\\d+)?;[a-zA-Z]+$") || recipeProductStatement.matches("^[a-zA-Z ]+;(-)?[1-9]+[0-9]*(.\\d+)?;[a-zA-Z]+$"))) {
                    return new ResponseMessage("Recipe product has to match format \"recipeProductName;recipeProductAmount;recipeProductUnit\". Recipe product name may contain letters and spaces, recipe product amount may contain digits optionally divided by dot and recipe product unit may have value: \"g\", \"dag\", \"kg\", \"pcs\", \"ml\" or \"l\"");
                } else {
                    return new ResponseMessage("Recipe product statement is valid");
                }
            case "recipeProductName":
                String recipeProductName = String.valueOf(item);

                if (productRepo.findByProductName(recipeProductName) == null) {
                    return new ResponseMessage("Recipe product name has to be proper");
                } else {
                    return new ResponseMessage("Recipe product name is valid");
                }
            case "recipeProductAmount":
                Double recipeProductAmount = Double.valueOf(item.toString());

                if (recipeProductAmount.toString().length() < 1 || recipeProductAmount.toString().length() > 20) {
                    return new ResponseMessage("Recipe product amount has to have min 1 and max 20 characters");
                } else if (recipeProductAmount <= 0) {
                    return new ResponseMessage("Recipe product amount has to be greater than 0");
                } else {
                    return new ResponseMessage("Recipe product amount is valid");
                }
            case "recipeProductUnit":
                String recipeProductUnit = String.valueOf(item);

                if (!recipeProductUnits.contains(recipeProductUnit)) {
                    return new ResponseMessage("Recipe product unit has to be proper");
                } else {
                    return new ResponseMessage("Recipe product unit is valid");
                }
            case "recipeSteps":
                List<String> recipeSteps = (List<String>) item;
                if (recipeSteps == null) {
                    return new ResponseMessage("Recipe steps has to be given");
                } else if (recipeSteps.isEmpty()) {
                    return new ResponseMessage("At least 1 recipe step is required");
                } else {
                    return new ResponseMessage("Recipe steps are valid");
                }
            case "recipeStepStatement":
                String recipeStepStatement = String.valueOf(item);
                if (recipeStepStatement.equals("")) {
                    return new ResponseMessage("Recipe step has to be defined");
                } else if (recipeStepStatement.length() < 3 || recipeStepStatement.length() > 50) {
                    return new ResponseMessage("Recipe step has to have min 3 and max 50 characters");
                } else if (!(recipeStepStatement.matches("^[a-zA-Z,. 0-9]+$"))) {
                    return new ResponseMessage("Recipe step has to match format \"recipeStep\" and contains only digits, lowercase and uppercase letters with spaces, commas and dots");
                } else {
                    return new ResponseMessage("Recipe step statement is valid");
                }
        }

        return new ResponseMessage("Invalid type");
    }

    private String getImageURL(Long recipeId) {
        String filename = "recipe" + recipeId + ".jpg";
        Path file = root.resolve(filename);
        String url = "";
        try {
            List<String> list2 = Files.walk(this.root, 1).filter(path -> !path.equals(this.root) && path.getFileName().toString().equals(filename)).map(this.root::relativize).map(path -> path.getFileName().toString()).collect(Collectors.toList());
            if (!list2.isEmpty()) {
                url = MvcUriComponentsBuilder
                        .fromMethodName(ImageController.class, "getFile", "recipe", file.getFileName().toString()).build().toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return url;
    }

    @Override
    public List<Recipes> getAllRecipes() {
        List<Recipes> list = new ArrayList<>();
        recipeRepo.findAll().forEach(list::add);

        for (Recipes recipe:list) {
            String url = getImageURL(recipe.getRecipeId());
            recipe.setRecipeImage(url);
            System.out.println(url);
        }
        return list;
    }
        //            UserCollectionRecipeResponse collectionRecipe = new UserCollectionRecipeResponse();
//            collectionRecipe.setRecipeId(recipe.getRecipeId());
//            collectionRecipe.setRecipeName(recipe.getRecipeName());
//
//            UserResponse recipeOwner = new UserResponse();
//            recipeOwner.setUserId(recipe.getRecipeOwner().getUserId());
//            recipeOwner.setName(recipe.getRecipeOwner().getName());
//            recipeOwner.setSurname(recipe.getRecipeOwner().getSurname());
//            recipeOwner.setUsername(recipe.getRecipeOwner().getUsername());
//            recipeOwner.setRole(recipe.getRecipeOwner().getRole());
//            recipeOwner.setCreationDate(recipe.getRecipeOwner().getCreationDate());
//            recipeOwner.setWeights(recipe.getRecipeOwner().getWeights());
//
//            collectionRecipe.setRecipeOwner(recipeOwner);
//            collectionRecipe.setCreationDate(recipe.getCreationDate());
//            collectionRecipe.setApprovalStatus(recipe.getApprovalStatus());
//            collectionRecipe.setAssessmentDate(recipe.getAssessmentDate());
//            collectionRecipe.setRejectExplanation(recipe.getRejectExplanation());
//            collectionRecipe.setRecipeImage(recipe.getRecipeImage());
//            collectionRecipe.setRecipeShared(recipe.getRecipeShared());
//            collectionRecipe.setRecipeSteps(recipe.getRecipeSteps());
//
//            List<RecipeCustomerSatisfactionResponse> customerSatisfactions = new ArrayList<>();
//
//            List<RecipeCustomerSatisfaction> recipeCustomerSatisfactions = recipe.getRecipeCustomerSatisfactions();
//
//            for (RecipeCustomerSatisfaction satisfaction: recipeCustomerSatisfactions) {
//                UserResponse user = new UserResponse();
//                user.setUserId(satisfaction.getCustomerSatisfactionOwner().getUserId());
//                user.setName(satisfaction.getCustomerSatisfactionOwner().getName());
//                user.setSurname(satisfaction.getCustomerSatisfactionOwner().getSurname());
//                user.setUsername(satisfaction.getCustomerSatisfactionOwner().getUsername());
//                user.setRole(satisfaction.getCustomerSatisfactionOwner().getRole());
//                user.setCreationDate(satisfaction.getCustomerSatisfactionOwner().getCreationDate());
//                user.setWeights(satisfaction.getCustomerSatisfactionOwner().getWeights());
//
//                agh.edu.pl.diet.payloads.response.RecipeCustomerSatisfactionResponse satisfactionResponse = new agh.edu.pl.diet.payloads.response.RecipeCustomerSatisfactionResponse();
//                satisfactionResponse.setRecipeCustomerSatisfactionId(satisfaction.getRecipeCustomerSatisfactionId());
//                satisfactionResponse.setCustomerSatisfactionOwner(user);
//                satisfactionResponse.setRecipeFavourite(satisfaction.getRecipeFavourite());
//                satisfactionResponse.setRecipeRating(satisfaction.getRecipeRating());
//
//                customerSatisfactions.add(satisfactionResponse);
//            }
//
//            collectionRecipe.setRecipeCustomerSatisfactions(customerSatisfactions);
//
//            List<RecipeProductResponse> recipeProductsResponses = new ArrayList<>();
//
//            List<RecipeProduct> recipeProducts = recipe.getRecipeProducts();
//
//            for (RecipeProduct product: recipeProducts) {
//
//                UserResponse productOwner = new UserResponse();
//                productOwner.setUserId(product.getProduct().getOwner().getUserId());
//                productOwner.setName(product.getProduct().getOwner().getName());
//                productOwner.setSurname(product.getProduct().getOwner().getSurname());
//                productOwner.setCreationDate(product.getProduct().getOwner().getCreationDate());
//                productOwner.setUsername(product.getProduct().getOwner().getUsername());
//                productOwner.setRole(product.getProduct().getOwner().getRole());
//                productOwner.setWeights(product.getProduct().getOwner().getWeights());
//
//                ProductResponse productResponse = new ProductResponse();
//                productResponse.setProductId(product.getProduct().getProductId());
//                productResponse.setProductName(product.getProduct().getProductName());
//                productResponse.setCategory(product.getProduct().getCategory());
//                productResponse.setCreationDate(product.getProduct().getCreationDate());
//                productResponse.setCalories(product.getProduct().getCalories());
//                productResponse.setProductImage(product.getProduct().getProductImage());
//                productResponse.setOwner(productOwner);
//                productResponse.setApprovalStatus(product.getProduct().getApprovalStatus());
//                productResponse.setAssessmentDate(product.getProduct().getAssessmentDate());
//                productResponse.setRejectExplanation(product.getProduct().getRejectExplanation());
//                productResponse.setNutrients(product.getProduct().getNutrients());
//
//                RecipeProductResponse recipeProductResponse = new RecipeProductResponse();
//                recipeProductResponse.setId(product.getId());
//                recipeProductResponse.setProductAmount(product.getProductAmount());
//                recipeProductResponse.setProductUnit(product.getProductUnit());
//                recipeProductResponse.setProduct(productResponse);
//
//                recipeProductsResponses.add(recipeProductResponse);
//            }
//
//            collectionRecipe.setRecipeProducts(recipeProductsResponses);
//
//            collectionRecipe.setRecipeSteps(recipe.getRecipeSteps());


    @Override
    public Recipes getRecipe(Long recipeId) {
        String url = getImageURL(recipeId);
        Recipes recipe = recipeRepo.findById(recipeId).get();
        recipe.setRecipeImage(url);
        return recipe;
    }

    @Override
    public List<Recipes> getRecipes(RecipeGetRequest recipeGetRequest) {

        RecipeSearchRequest recipeSearchRequest = new RecipeSearchRequest();
        recipeSearchRequest.setPhrase(recipeGetRequest.getPhrase());

        List<Recipes> recipesList = searchRecipes(recipeSearchRequest);

        switch (recipeGetRequest.getRecipesGroup()) {
            case "personal":
                User currentLoggedUser = userService.findByUsername(userService.getLoggedUser().getUsername());
                if (currentLoggedUser == null) {
                    return new ArrayList<>();
                }

                RecipeCollection collection = recipeCollectionRepo.findByRecipeCollector(currentLoggedUser);
                if (collection == null) {
                    return new ArrayList<>();
                }

                recipesList = recipesList.stream().filter(recipe -> collection.getRecipeCollection().contains(recipe)).collect(Collectors.toList());
                recipesList.stream().map(Recipes::getRecipeName).forEach(System.out::println);
                break;
            case "shared":
                recipesList = recipesList.stream().filter(Recipes::getRecipeShared).collect(Collectors.toList());
                break;
            case "unconfirmed":
                recipesList = recipesList.stream().filter(recipe -> recipe.getApprovalStatus().equals("pending") || recipe.getApprovalStatus().equals("rejected")).collect(Collectors.toList());
                break;
            case "accepted":
                recipesList = recipesList.stream().filter(recipe -> recipe.getApprovalStatus().equals("accepted")).collect(Collectors.toList());
                break;
            case "pending":
                recipesList = recipesList.stream().filter(recipe -> recipe.getApprovalStatus().equals("pending")).collect(Collectors.toList());
                break;
            case "rejected":
                recipesList = recipesList.stream().filter(recipe -> recipe.getApprovalStatus().equals("rejected")).collect(Collectors.toList());
                break;
            default:
                recipesList = null;
        }

        return recipesList;
    }

    @Override
    public ResponseMessage addNewRecipe(RecipeRequest recipeRequest) {
        Recipes recipe = new Recipes();
        String recipeName = recipeRequest.getRecipeName();

        ResponseMessage responseMessage = verify("name", recipeName);
        if (responseMessage.getMessage().equals("Recipe name is valid")){
            recipe.setRecipeName(recipeName);
        } else {
            return responseMessage;
        }

        List<String> recipeProducts = recipeRequest.getRecipeProducts();

        ResponseMessage responseMessage4 = verify("recipeProducts", recipeProducts);

        if (!(responseMessage4.getMessage().equals("Recipe products are valid"))) {
            return responseMessage4;
        }

        for (String recipeProductStatement: recipeProducts) {

            ResponseMessage responseMessage5 = verify("recipeProductStatement", recipeProductStatement);

            if (!(responseMessage5.getMessage().equals("Recipe product statement is valid"))) {
                return responseMessage5;
            }
            String[] parts = recipeProductStatement.split(";");
            String recipeProductName = parts[0];
            Double recipeProductAmount = Double.valueOf(parts[1]);
            String recipeProductUnit = parts[2];

            ResponseMessage responseMessage6 = verify("recipeProductName", recipeProductName);

            if (!(responseMessage6.getMessage().equals("Recipe product name is valid"))) {
                return responseMessage6;
            }

            ResponseMessage responseMessage7 = verify("recipeProductAmount", recipeProductAmount);

            if (!(responseMessage7.getMessage().equals("Recipe product amount is valid"))) {
                return responseMessage7;
            }

            ResponseMessage responseMessage8 = verify("recipeProductUnit", recipeProductUnit);

            if (!(responseMessage8.getMessage().equals("Recipe product unit is valid"))) {
                return responseMessage8;
            }

            Product product = productRepo.findByProductName(recipeProductName);
            RecipeProduct recipeProduct = new RecipeProduct();
            recipeProduct.setProduct(product);
            recipeProduct.setProductAmount(recipeProductAmount);
            recipeProduct.setProductUnit(recipeProductUnit);
            recipeProduct.setRecipe(recipe);
            recipe.addRecipeProduct(recipeProduct);
        }

        List<String> recipeSteps = recipeRequest.getRecipeSteps();

        ResponseMessage responseMessage9 = verify("recipeSteps", recipeSteps);

        if (!(responseMessage9.getMessage().equals("Recipe steps are valid"))) {
            return responseMessage9;
        }

        for (String recipeStepStatement: recipeSteps) {

            ResponseMessage responseMessage10 = verify("recipeStepStatement", recipeStepStatement);

            if (!(responseMessage10.getMessage().equals("Recipe step statement is valid"))) {
                return responseMessage10;
            }

            RecipeStep recipeStep = new RecipeStep();
            recipeStep.setRecipeStepDescription(recipeStepStatement);
            recipeStep.setRecipe(recipe);
            recipe.addRecipeStep(recipeStep);
        }

        //change to logged in user id
        User recipeOwner = userService.findByUsername(userService.getLoggedUser().getUsername());
        if (recipeOwner == null) {
            return new ResponseMessage("Recipe " + recipeName + " owner has not been found");
        }

        recipe.setRecipeOwner(recipeOwner);
        String creationDate = new Date().toInstant().toString();
        recipe.setCreationDate(creationDate);

        RecipeCollection recipeCollection = recipeCollectionRepo.findByRecipeCollector(recipeOwner);
        if (recipeCollection == null) {
            recipeCollection = new RecipeCollection(recipeOwner);
        }
        recipeCollection.addRecipeToCollection(recipe);

        recipeCollectionRepo.save(recipeCollection);
        recipeRepo.save(recipe);

        Recipes lastAddedRecipe = getAllRecipes().stream().filter(p -> p.getCreationDate().equals(creationDate)).findFirst().orElse(null);
        if (lastAddedRecipe != null) {
            return new ResponseMessage(lastAddedRecipe.getRecipeId() + " Recipe " + recipe.getRecipeName() + " has been added successfully");
        } else {
            return new ResponseMessage(" Recipe " + recipe + " has not been found");
        }
    }

    @Override
    public ResponseMessage updateRecipes(Long recipeId, RecipeRequest recipeRequest) {

        String recipeName = "";
        Optional<Recipes> recipe = recipeRepo.findById(recipeId);
        if (recipe.isPresent()) {
            Recipes updatedRecipes = recipe.get();
            recipeName = recipeRequest.getRecipeName();

            ResponseMessage responseMessage = verify("name", recipeName);
            if (responseMessage.getMessage().equals("Recipe name is valid")){
                updatedRecipes.setRecipeName(recipeName);
            } else {
                return responseMessage;
            }

//            Integer calories = productRequest.getCalories();
//
//            ResponseMessage responseMessage2 = verify("update", "calories", calories);
//
//            if (responseMessage2.getMessage().equals("Product calories are valid")) {
//                updatedProduct.setCalories(calories);
//            } else {
//                return responseMessage2;
//            }
//
//            String categoryName = productRequest.getCategory();
//
//            ResponseMessage responseMessage3 = verify("update", "category", categoryName);
//
//            if (responseMessage3.getMessage().equals("Product category is valid")) {
//                updatedProduct.setCategory(categoryRepo.findByCategoryName(categoryName));
//            } else {
//                return responseMessage3;
//            }

            List<String> products = new ArrayList<>();

            ResponseMessage responseMessage4 = verify( "list", products);

            if (!(responseMessage4.getMessage().equals("Product are valid"))) {
                return responseMessage4;
            }

            for (String productStatement: products) {

                ResponseMessage responseMessage5 = verify("productStatement", productStatement);

                if (!(responseMessage5.getMessage().equals("Product statement is valid"))) {
                    return responseMessage5;
                }
                String[] parts = productStatement.split(";");
                String productName = parts[0];
                Double productAmount = Double.valueOf(parts[1]);

                ResponseMessage responseMessage6 = verify("productName", productName);

                if (!(responseMessage6.getMessage().equals("Product name is valid"))) {
                    return responseMessage6;
                }

                ResponseMessage responseMessage7 = verify("productAmount", productAmount);

                if (!(responseMessage7.getMessage().equals("Product amount is valid"))) {
                    return responseMessage7;
                }

                RecipeProduct recipeProduct = updatedRecipes.getRecipeProducts().stream().filter(pn -> pn.getRecipe().getRecipeName().equals(productName)).findFirst().orElse(null);

                if (recipeProduct != null) {
                    recipeProduct.setProductAmount(productAmount);
                } else {
                    return new ResponseMessage( "Product " + productName + " belonging to product " + recipeName + " has not been found");
                }
            }
            recipeRepo.save(updatedRecipes);

            return new ResponseMessage("Recipe " + recipeName + " has been updated successfully");
        }
        return new ResponseMessage("Recipe " + recipeName + " has not been found");
    }

    @Override
    public ResponseMessage removeRecipes(Long recipeId) {

        Optional<Recipes> recipes = recipeRepo.findById(recipeId);
        if (recipes.isPresent()) {
            Recipes removedRecipe = recipes.get();
            recipeRepo.delete(removedRecipe);
            return new ResponseMessage("Product " + removedRecipe.getRecipeName() + " has been removed successfully");
        }
        return new ResponseMessage("Recipe id " + recipeId + " has not been found");
    }

    @Override
    public List<Recipes> searchRecipes(RecipeSearchRequest recipeSearchRequest) {

        String phrase = recipeSearchRequest.getPhrase();

        List<Recipes> recipes = getAllRecipes();

        if (!phrase.equals("")) {
            recipes = recipes.stream().filter(recipe -> recipe.getRecipeName().toLowerCase().contains(phrase.toLowerCase())).collect(Collectors.toList());
        }
        return recipes;
    }

    @Override
    public ResponseMessage serveRecipeCustomerSatisfaction(Long recipeId, String type, Float rating) {
        if (recipeId == null) {
            return new ResponseMessage("recipeId cannot be null");
        }
        User currentLoggedUser = userService.findByUsername(userService.getLoggedUser().getUsername());
        if (currentLoggedUser == null) {
            return new ResponseMessage("Current logged user has not been found");
        }
        Recipes recipe = recipeRepo.findByRecipeId(recipeId);
        if (recipe == null) {
            return new ResponseMessage("Recipe marked id " + recipeId + " has not been found");
        } else if (!recipe.getApprovalStatus().equals("accepted")) {
            return new ResponseMessage("Only accepted recipe might be rated and marked as favourite");
        }
        RecipeCustomerSatisfaction recipeCustomerSatisfaction = recipeCustomerSatisfactionsRepo.findByCustomerSatisfactionOwnerAndRecipe(currentLoggedUser, recipe);

        if (recipeCustomerSatisfaction == null) {
            recipeCustomerSatisfaction = new RecipeCustomerSatisfaction(recipe, currentLoggedUser);
        }

        if (type.equals("rating")) {
            recipeCustomerSatisfaction.setRecipeRating(rating);
            recipeCustomerSatisfactionsRepo.save(recipeCustomerSatisfaction);
            return new ResponseMessage("Recipe with id " + recipeId + " has been rated with " + rating + " grade");
        }

        recipeCustomerSatisfaction.setRecipeFavourite(!recipeCustomerSatisfaction.getRecipeFavourite());
        recipeCustomerSatisfactionsRepo.save(recipeCustomerSatisfaction);

        String mark = recipeCustomerSatisfaction.getRecipeFavourite() ? "marked" : "unmarked";

        return new ResponseMessage("Recipe with id " + recipeId + " has been " + mark + " as favourite");
    }

    @Override
    public ResponseMessage assessRecipe(ItemAssessRequest itemAssessRequest) {
        Recipes assessedRecipe = recipeRepo.findById(itemAssessRequest.getItemId()).orElse(null);

        if (assessedRecipe != null) {

            if (!assessedRecipe.getApprovalStatus().equals("pending")) {
                return new ResponseMessage("Recipe has been assessed yet");
            }

            String assessment = itemAssessRequest.getAssessment();
            String rejectExplanation = itemAssessRequest.getRejectExplanation();
            String assessmentDate = new Date().toInstant().toString();
            assessedRecipe.setAssessmentDate(assessmentDate);

            switch (assessment) {
                case "accept":
                    assessedRecipe.setApprovalStatus("accepted");
                    if (rejectExplanation != null) {
                        return new ResponseMessage("Rejection explanation is only required for recipe rejection");
                    }
                    recipeRepo.save(assessedRecipe);
                    return new ResponseMessage("Recipe " + assessedRecipe.getRecipeName() + " has been accepted");
                case "reject":
                    assessedRecipe.setApprovalStatus("rejected");
                    if (rejectExplanation == null || rejectExplanation.equals("")) {
                        return new ResponseMessage("Rejection explanation is required for recipe rejection");
                    }
                    assessedRecipe.setRejectExplanation(rejectExplanation);
                    recipeRepo.save(assessedRecipe);
                    return new ResponseMessage("Recipe " + assessedRecipe.getRecipeName() + " has been rejected");
                default:
                    return new ResponseMessage("Wrong assessment");
            }
        }
        return new ResponseMessage("Proper recipe has not been found");
    }

}