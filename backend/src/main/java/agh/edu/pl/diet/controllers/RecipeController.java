package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.Recipes;
import agh.edu.pl.diet.payloads.request.*;
import agh.edu.pl.diet.payloads.response.RecipeResponse;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.payloads.response.SuitabilityRecipeResponse;
import agh.edu.pl.diet.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public List<Recipes> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @PostMapping
    public ResponseEntity<List<Recipes>> getRecipes(@RequestBody RecipeGetRequest recipeGetRequest) {
        List<Recipes> recipesList = recipeService.getRecipes(recipeGetRequest);
        if (recipesList == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(recipesList);
    }

    @PostMapping("/getRecipesSuitabilities")
    public ResponseEntity<List<RecipeResponse>> getRecipesSuitabilities(@RequestBody RecipeGetRequest recipeGetRequest) {
        List<RecipeResponse> recipesList = recipeService.getRecipesSuitabilities(recipeGetRequest);
        if (recipesList == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(recipesList);
    }

    @GetMapping("/{id}")
    public Recipes getRecipe(@PathVariable("id") Long recipeId) {
        return recipeService.getRecipe(recipeId);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addNewRecipes(@RequestBody RecipeRequest recipeRequest) {
        ResponseMessage message = recipeService.addNewRecipe(recipeRequest);
        if (message.getMessage().endsWith(" has been added successfully")) {
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseMessage updateRecipe(@PathVariable("id") Long recipeId, @RequestBody RecipeRequest recipeRequest) {
        return recipeService.updateRecipe(recipeId, recipeRequest);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ResponseMessage> removeRecipe(@PathVariable("id") Long recipeId) {
        ResponseMessage message = recipeService.removeRecipe(recipeId);
        if (!message.getMessage().endsWith("has been removed successfully")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Recipes>> searchRecipes(@RequestBody RecipeSearchRequest recipeSearchRequest) {
        List<Recipes> recipes = recipeService.searchRecipes(recipeSearchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(recipes);
    }

    @PutMapping("/rate/{id}/{rating}")
    public ResponseEntity<ResponseMessage> rateRecipe(@PathVariable("id") Long recipeId, @PathVariable("rating") Float rating) {
        ResponseMessage responseMessage = recipeService.serveRecipeCustomerSatisfaction(recipeId, "rating", rating);
        if (!responseMessage.getMessage().equals("Recipe with id " + recipeId + " has been rated with " + rating + " grade")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PutMapping("/markFavourite/{id}")
    public ResponseEntity<ResponseMessage> markRecipeFavourite(@PathVariable("id") Long recipeId) {
        ResponseMessage responseMessage = recipeService.serveRecipeCustomerSatisfaction(recipeId, "favourite", null);
        if (!responseMessage.getMessage().equals("Recipe with id " + recipeId + " has been marked as favourite") && !responseMessage.getMessage().equals("Recipe with id " + recipeId + " has been unmarked as favourite")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PostMapping("/assess")
    public ResponseEntity<ResponseMessage> assessProduct(@RequestBody ItemAssessRequest recipeAssessRequest) {
        ResponseMessage responseMessage = recipeService.assessRecipe(recipeAssessRequest);
        if (!responseMessage.getMessage().endsWith("has been accepted") && !responseMessage.getMessage().endsWith("has been rejected")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @GetMapping("/productUnits")
    public List<String> getAllUnits() {
        return recipeService.getAllUnits();
    }

    @GetMapping("/share/{id}")
    public ResponseEntity<ResponseMessage> shareRecipe(@PathVariable("id") Long recipeId) {
        ResponseMessage responseMessage = recipeService.shareRecipe(recipeId);
        if (!responseMessage.getMessage().endsWith("has been shared") && !responseMessage.getMessage().endsWith("has been unshared")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @GetMapping("/addToCollection/{id}")
    public ResponseEntity<ResponseMessage> addRecipeToCollection(@PathVariable("id") Long recipeId) {
        ResponseMessage responseMessage = recipeService.addRecipeToCollection(recipeId);
        if (!responseMessage.getMessage().endsWith("has been added to collection") && !responseMessage.getMessage().endsWith("has been removed from collection")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @GetMapping("/checkIfInCollection/{id}")
    public ResponseEntity<ResponseMessage> checkIfInCollection(@PathVariable("id") Long recipeId) {
        ResponseMessage responseMessage = recipeService.checkIfInCollection(recipeId);
        if (!responseMessage.getMessage().endsWith("is in collection") && !responseMessage.getMessage().endsWith("is not in collection")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @GetMapping("/checkRecipeSuitability/{id}")
    public ResponseEntity<List<List<SuitabilityRecipeResponse>>> checkRecipeSuitability(@PathVariable("id") Long recipeId) {
        List<List<SuitabilityRecipeResponse>> response = recipeService.checkRecipeSuitability(recipeId);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}