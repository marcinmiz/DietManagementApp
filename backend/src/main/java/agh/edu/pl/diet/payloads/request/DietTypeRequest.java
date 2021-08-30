package agh.edu.pl.diet.payloads.request;

import agh.edu.pl.diet.exceptions.InvalidInputException;

import javax.validation.constraints.NotBlank;

public class DietTypeRequest {
    @NotBlank
    private String dietTypeName;
    @NotBlank
    private Integer calories;
    @NotBlank
    private Integer protein;
    @NotBlank
    private Integer carbohydrates;
    @NotBlank
    private Integer fats;
    @NotBlank
    protected double targetWeight;


    public String getDietTypeName() {
        return dietTypeName;
    }

    public void setDietTypeName(String dietTypeName) {
        this.dietTypeName = dietTypeName;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public Integer getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Integer carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Integer getFats() {
        return fats;
    }

    public void setFats(Integer fats) {
        this.fats = fats;
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(double newTargetWeight) throws InvalidInputException {
        if (newTargetWeight < 0) {
            throw new InvalidInputException();
        }
        targetWeight = newTargetWeight; //
    }}
