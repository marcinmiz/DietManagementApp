package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;

public class RecipeGetRequest {
    @NotBlank
    private String all;
    @NotBlank
    private Integer groupNumber;
    @NotBlank
    private String recipesGroup;
    @NotBlank
    private String phrase;

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

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
