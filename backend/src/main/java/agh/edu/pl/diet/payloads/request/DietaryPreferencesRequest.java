package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class DietaryPreferencesRequest {
    @NotBlank
    private Boolean dietTypeSelected;
    @NotBlank
    private Integer totalDailyCalories;
    @NotBlank
    private String dietType;
    @NotBlank
    private Integer caloriesPerMeal;
    @NotBlank
    private Integer mealsQuantity;
    @NotBlank
    private Double targetWeight;
    @NotBlank
    private List<String> nutrients;
    @NotBlank
    private List<String> products;
    @NotBlank
    private List<String> recipes;

    public Boolean getDietTypeSelected() {
        return dietTypeSelected;
    }

    public void setDietTypeSelected(Boolean dietTypeSelected) {
        this.dietTypeSelected = dietTypeSelected;
    }

    public Integer getTotalDailyCalories() {
        return totalDailyCalories;
    }

    public void setTotalDailyCalories(Integer totalDailyCalories) {
        this.totalDailyCalories = totalDailyCalories;
    }

    public String getDietType() {
        return dietType;
    }

    public void setDietType(String dietType) {
        this.dietType = dietType;
    }

    public Integer getCaloriesPerMeal() {
        return caloriesPerMeal;
    }

    public void setCaloriesPerMeal(Integer caloriesPerMeal) {
        this.caloriesPerMeal = caloriesPerMeal;
    }

    public Integer getMealsQuantity() {
        return mealsQuantity;
    }

    public void setMealsQuantity(Integer mealsQuantity) {
        this.mealsQuantity = mealsQuantity;
    }

    public Double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(Double targetWeight) {
        this.targetWeight = targetWeight;
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
