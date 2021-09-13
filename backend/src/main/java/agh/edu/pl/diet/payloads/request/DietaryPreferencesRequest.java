package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class DietaryPreferencesRequest {
    @NotBlank
    private Long dietaryPreferenceId;
    @NotBlank
    private Integer totalCalories;
    @NotBlank
    private String dietType;
    @NotBlank
    private String preferenceOwner;
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

    public Long getDietaryPreferenceId() {
        return dietaryPreferenceId;
    }

    public void setDietaryPreferenceId(Long dietaryPreferenceId) {
        this.dietaryPreferenceId = dietaryPreferenceId;
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

    public String getPreferenceOwner() {
        return preferenceOwner;
    }

    public void setPreferenceOwner(String preferenceOwner) {
        this.preferenceOwner = preferenceOwner;
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
