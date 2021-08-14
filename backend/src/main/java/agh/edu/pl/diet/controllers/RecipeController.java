package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.Recipes;
import agh.edu.pl.diet.payloads.request.RecipeRequest;
import agh.edu.pl.diet.payloads.request.RecipeSearchRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public List<Recipes> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    public Recipes getRecipe(@PathVariable("id") Long recipeId) {
        return recipeService.getRecipes(recipeId);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addNewRecipes(@RequestBody RecipeRequest recipeRequest) {
        ResponseMessage message = recipeService.addNewRecipes(recipeRequest);
        if (message.getMessage().endsWith(" has been added successfully")) {
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseMessage updateRecipes(@PathVariable("id") Long recipeId, @RequestBody RecipeRequest recipeRequest) {
        return recipeService.updateRecipes(recipeId, recipeRequest);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseMessage removeRecipes(@PathVariable("id") Long recipeId) {
        return recipeService.removeRecipes(recipeId);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Recipes>> searchRecipes(@RequestBody RecipeSearchRequest recipeSearchRequest) {
        List<Recipes> recipes = recipeService.searchRecipes(recipeSearchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(recipes);
    }

}