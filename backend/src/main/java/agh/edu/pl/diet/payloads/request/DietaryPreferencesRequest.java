package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class DietaryPreferencesRequest {
    @NotBlank
    private String dietaryPreferenceName;
    @NotBlank
    private Integer totalCalories;
    @NotBlank
    private String dietType;
    @NotBlank
    private String owner;
    @NotBlank
    private List<String> nutrients;
    @NotBlank
    private List<String> products;
    @NotBlank
    private List<String> recipes;

    public String getDietaryPreferenceName() {
        return dietaryPreferenceName;
    }

    public void setDietaryPreferenceName(String dietaryPreferenceName) {
        this.dietaryPreferenceName = dietaryPreferenceName;
    }

    public Integer getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(Integer totalCalories) {
        this.totalCalories = totalCalories;
    }

    public String getDietType() {
        return dietType;
    }

    public void setDietType(String dietType) {
        this.dietType = dietType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getNutrients() {
        return nutrients;
    }

    public void setNutrients(List<String> nutrients) {
        this.nutrients = nutrients;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public List<String> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<String> recipes) {
        this.recipes = recipes;
    }
}
