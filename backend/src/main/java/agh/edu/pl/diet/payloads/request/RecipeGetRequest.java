package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;

public class RecipeGetRequest {
    @NotBlank
    private String recipesGroup;
    @NotBlank
    private String phrase;

    public String getRecipesGroup() {
        return recipesGroup;
    }

    public void setRecipesGroup(String recipesGroup) {
        this.recipesGroup = recipesGroup;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

}
