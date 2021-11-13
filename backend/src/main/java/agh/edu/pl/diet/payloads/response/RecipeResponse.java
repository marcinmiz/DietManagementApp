package agh.edu.pl.diet.payloads.response;

import agh.edu.pl.diet.entities.Recipes;

public class RecipeResponse {
    private Recipes recipe;
    private Integer positiveSuitabilities;
    private Integer negativeSuitabilities;

    public RecipeResponse(Recipes recipe) {
        this.recipe = recipe;
    }

    public RecipeResponse(Recipes recipe, Integer positiveSuitabilities, Integer negativeSuitabilities) {
        this.recipe = recipe;
        this.positiveSuitabilities = positiveSuitabilities;
        this.negativeSuitabilities = negativeSuitabilities;
    }

    public Recipes getRecipe() {
        return recipe;
    }

    public Integer getPositiveSuitabilities() {
        return positiveSuitabilities;
    }

    public void setPositiveSuitabilities(Integer positiveSuitabilities) {
        this.positiveSuitabilities = positiveSuitabilities;
    }

    public Integer getNegativeSuitabilities() {
        return negativeSuitabilities;
    }

    public void setNegativeSuitabilities(Integer negativeSuitabilities) {
        this.negativeSuitabilities = negativeSuitabilities;
    }
}
