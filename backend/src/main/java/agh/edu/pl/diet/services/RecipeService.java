package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.Recipes;
import agh.edu.pl.diet.payloads.request.ItemAssessRequest;
import agh.edu.pl.diet.payloads.request.RecipeGetRequest;
import agh.edu.pl.diet.payloads.request.RecipeRequest;
import agh.edu.pl.diet.payloads.request.RecipeSearchRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.payloads.response.UserCollectionRecipeResponse;

import java.util.List;

public interface RecipeService {

    List<Recipes> getAllRecipes();

    Recipes getRecipe(Long recipe_id);

    List<Recipes> getRecipes(RecipeGetRequest recipeGetRequest);

    ResponseMessage addNewRecipe(RecipeRequest recipesRequest);

    ResponseMessage updateRecipes(Long recipeId, RecipeRequest recipeRequest);

    ResponseMessage removeRecipes(Long recipeId);

    List<Recipes> searchRecipes(RecipeSearchRequest recipeSearchRequest);

    ResponseMessage serveRecipeCustomerSatisfaction(Long recipeId, String type, Float rating);

    ResponseMessage assessRecipe(ItemAssessRequest itemAssessRequest);

}
