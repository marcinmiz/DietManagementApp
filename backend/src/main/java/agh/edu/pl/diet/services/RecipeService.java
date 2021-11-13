package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.Recipes;
import agh.edu.pl.diet.payloads.request.ItemAssessRequest;
import agh.edu.pl.diet.payloads.request.RecipeGetRequest;
import agh.edu.pl.diet.payloads.request.RecipeRequest;
import agh.edu.pl.diet.payloads.request.RecipeSearchRequest;
import agh.edu.pl.diet.payloads.response.RecipeResponse;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.payloads.response.SuitabilityRecipeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public interface RecipeService {

    List<Recipes> getAllRecipes();

    Recipes getRecipe(Long recipe_id);

    List<Recipes> getRecipes(RecipeGetRequest recipeGetRequest);

    List<RecipeResponse> getRecipesSuitabilities(RecipeGetRequest recipeGetRequest);

    ResponseMessage addNewRecipe(RecipeRequest recipesRequest);

    ResponseMessage updateRecipe(Long recipeId, RecipeRequest recipeRequest);

    ResponseMessage removeRecipe(Long recipeId);

    List<Recipes> searchRecipes(RecipeSearchRequest recipeSearchRequest);

    ResponseMessage serveRecipeCustomerSatisfaction(Long recipeId, String type, Float rating);

    ResponseMessage assessRecipe(ItemAssessRequest itemAssessRequest);

    List<String> getAllUnits();

    ResponseMessage shareRecipe(Long recipeId);

    ResponseMessage addRecipeToCollection(Long recipeId);

    ResponseMessage checkIfInCollection(Long recipeId);

    List<List<SuitabilityRecipeResponse>> checkRecipeSuitability(Long recipeId);
}
