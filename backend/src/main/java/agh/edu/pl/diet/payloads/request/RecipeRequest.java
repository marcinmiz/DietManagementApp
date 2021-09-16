package agh.edu.pl.diet.payloads.request;

import agh.edu.pl.diet.entities.User;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class RecipeRequest {

    @NotBlank
    private String recipeName;
    @NotBlank
    private List<String> recipeProducts;
    @NotBlank
    private List<String> recipeSteps;

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public List<String> getRecipeProducts() {
        return recipeProducts;
    }

    public void setRecipeProducts(List<String> recipeProducts) {
        this.recipeProducts = recipeProducts;
    }

    public void addRecipeProduct(String recipeProduct) {
        this.recipeProducts.add(recipeProduct);
    }

    public List<String> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(List<String> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public void addStep(String recipeStep) {
        this.recipeSteps.add(recipeStep);
    }
}
