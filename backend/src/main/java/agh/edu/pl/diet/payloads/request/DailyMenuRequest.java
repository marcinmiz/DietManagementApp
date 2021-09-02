package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class DailyMenuRequest {
    @NotBlank
    private String dailyMenuName;
    @NotBlank
    private String dailyMenuDate;
    @NotBlank
    private Integer mealsQuantity;
    @NotBlank
    private List<String> meals;

    public String getDailyMenuName() {
        return dailyMenuName;
    }

    public void setDailyMenuName(String dailyMenuName) {
        this.dailyMenuName = dailyMenuName;
    }

    public String getDailyMenuDate() {
        return dailyMenuDate;
    }

    public void setDailyMenuDate(String dailyMenuDate) {
        this.dailyMenuDate = dailyMenuDate;
    }

    public Integer getMealsQuantity() {
        return mealsQuantity;
    }

    public void setMealsQuantity(Integer mealsQuantity) {
        this.mealsQuantity = mealsQuantity;
    }

    public List<String> getMeals() {
        return meals;
    }

    public void setMeals(List<String> meals) {
        this.meals = meals;
    }
}
