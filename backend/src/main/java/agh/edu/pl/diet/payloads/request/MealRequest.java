package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class MealRequest {
    @NotBlank
    private String mealName;
    @NotBlank
    private String mealHourTime;
    @NotBlank
    private List<String> dailyMenu;
    @NotBlank
    private List<String> recipe;

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealHourTime() {
        return mealHourTime;
    }

    public void setMealHourTime(String mealHourTime) {
        this.mealHourTime = mealHourTime;
    }

    public List<String> getDailyMenu() {
        return dailyMenu;
    }

    public void setDailyMenu(List<String> dailyMenu) {
        this.dailyMenu = dailyMenu;
    }

    public List<String> getRecipe() {
        return recipe;
    }

    public void setRecipe(List<String> recipe) {
        this.recipe = recipe;
    }
}