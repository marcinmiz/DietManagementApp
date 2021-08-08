package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.Product;
import agh.edu.pl.diet.entities.Recipes;
import agh.edu.pl.diet.payloads.request.RecipeRequest;
import agh.edu.pl.diet.payloads.request.RecipeSearchRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;

import java.util.List;

public interface RecipeService {

    List<Product> getAllRecipes();

    Recipes getRecipes(Long recipe_id);

    ResponseMessage addNewRecipes(RecipeRequest recipesRequest);

    ResponseMessage updateRecipes(Long recipeId, RecipeRequest recipeRequest);

    ResponseMessage removeRecipes(Long recipeId);

    List<Recipes> searchRecipes(RecipeSearchRequest recipeSearchRequest);

    void removeOne(Long id);
}
