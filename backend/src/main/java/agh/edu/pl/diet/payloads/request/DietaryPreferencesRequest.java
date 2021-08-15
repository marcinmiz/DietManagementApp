package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;

public class DietaryPreferencesRequest {
    @NotBlank
    private String dietaryPreferenceName;
    @NotBlank
    private Integer totalCalories;
    @NotBlank
    private String dietType;
    @NotBlank
    private String owner;

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
}
