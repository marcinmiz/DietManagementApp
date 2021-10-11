package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.controllers.ImageController;
import agh.edu.pl.diet.entities.RecipeProduct;
import agh.edu.pl.diet.entities.Recipes;
import agh.edu.pl.diet.payloads.request.RecipeRequest;
import agh.edu.pl.diet.payloads.request.RecipeSearchRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.ProductRepo;
import agh.edu.pl.diet.repos.RecipeRepo;
import agh.edu.pl.diet.repos.UserRepo;
import agh.edu.pl.diet.services.RecipeService;
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

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private RecipeRepo recipeRepo;
    @Autowired
    private UserRepo userRepo;

    private ResponseMessage verify(String mode, String type, Object item) {
        switch (type) {
            case "name":
                String name = String.valueOf(item);
                if (name == null) {
                    return new ResponseMessage("Recipe name has to be given");
                } else if (name.length() < 2 || name.length() > 40) {
                    return new ResponseMessage("Recipe name has to have min 2 and max 40 characters");
                } else if (!(name.matches("^[a-zA-Z ]+$"))) {
                    return new ResponseMessage("Recipe name has to contain only letters and spaces");
                } else if (!mode.equals("update") && recipeRepo.findByRecipeName(name) != null) {
                    return new ResponseMessage("Recipe with this name exists yet");
                } else {
                    return new ResponseMessage("Recipe name is valid");
                }

            case "list":
                List<String> products = (List<String>) item;
                if (products == null) {
                    return new ResponseMessage("Products has to be given");
                } else if (products.isEmpty()) {
                    return new ResponseMessage("At least 1 product products is required");
                } else {
                    return new ResponseMessage("Products are valid");
                }

            case "owner":
                String owner = String.valueOf(item);

                if (userRepo.findByUsername(owner) == null) {
                    return new ResponseMessage("Owner name has to be proper");
                } else {
                    return new ResponseMessage("Owner name is valid");
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
                        .fromMethodName(ImageController.class, "getFile", file.getFileName().toString()).build().toString();
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
        }
        return list;
    }

    @Override
    public Recipes getRecipes(Long recipeId) {
        String url = getImageURL(recipeId);
        Recipes recipe = recipeRepo.findById(recipeId).get();
        recipe.setRecipeImage(url);
        return recipe;
    }

    @Override
    public ResponseMessage addNewRecipes(RecipeRequest recipeRequest) {
        Recipes recipe = new Recipes();
        String recipeName = recipeRequest.getRecipeName();

        ResponseMessage responseMessage = verify("add", "name", recipeName);
        if (responseMessage.getMessage().equals("Product name is valid")){
            recipe.setRecipeName(recipeName);
        } else {
            return responseMessage;
        }

        String recipeNames = recipeRequest.getRecipeName();

        ResponseMessage responseMessage3 = verify("add", "recipes", recipeNames);

        if (responseMessage3.getMessage().equals("Recipe is valid")) {
            recipe.setRecipeName(String.valueOf(recipeRepo.findByRecipeName(recipeNames)));
        } else {
            return responseMessage3;
        }

        //change to logged in user id
        recipe.setOwner(userRepo.findById(256L).get());
        String creationDate = new Date().toInstant().toString();
        recipe.setCreationDate(creationDate);

        recipeRepo.save(recipe);
        Recipes lastAddedRecipe = getAllRecipes().stream().filter(p -> p.getCreationDate().equals(creationDate)).findFirst().orElse(null);
        if (lastAddedRecipe != null) {
            return new ResponseMessage(lastAddedRecipe.getRecipeId() + " Recipe " + recipe + " has been added successfully");
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

            ResponseMessage responseMessage = verify("update", "name", recipeName);
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

            List<String> products = Collections.singletonList(recipeRequest.getProduct());

            ResponseMessage responseMessage4 = verify("update", "list", products);

            if (!(responseMessage4.getMessage().equals("Product are valid"))) {
                return responseMessage4;
            }

            for (String productStatement: products) {

                ResponseMessage responseMessage5 = verify("update", "productStatement", productStatement);

                if (!(responseMessage5.getMessage().equals("Product statement is valid"))) {
                    return responseMessage5;
                }
                String[] parts = productStatement.split(";");
                String productName = parts[0];
                Double productAmount = Double.valueOf(parts[1]);

                ResponseMessage responseMessage6 = verify("update", "productName", productName);

                if (!(responseMessage6.getMessage().equals("Product name is valid"))) {
                    return responseMessage6;
                }

                ResponseMessage responseMessage7 = verify("update", "productAmount", productAmount);

                if (!(responseMessage7.getMessage().equals("Product amount is valid"))) {
                    return responseMessage7;
                }

                RecipeProduct recipeProduct = updatedRecipes.getRecipes().stream().filter(pn -> pn.getRecipe().getRecipeName().equals(productName)).findFirst().orElse(null);

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

//        String category = recipeSearchRequest.getCategory();
        String phrase = recipeSearchRequest.getPhrase();

        List<Recipes> products = getAllRecipes();

//        if (!category.equals("")) {
//            List<Category> categories = new ArrayList<>();
//            categoryRepo.findAll().forEach(categories::add);
//            if (categories.stream().map(Category::getCategoryName).filter(n -> n.equalsIgnoreCase(category)).findFirst().orElse(null) == null) {
//                return new ArrayList<>();
//            }
//            products = products.stream().filter(product -> product.getCategory().getCategoryName().equalsIgnoreCase(category)).collect(Collectors.toList());
//        }

        if (!phrase.equals("")) {
            products = products.stream().filter(product -> product.getRecipeName().toLowerCase().contains(phrase.toLowerCase())).collect(Collectors.toList());
        }
        return products;
    }
}