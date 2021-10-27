package agh.edu.pl.diet.payloads.response;

import agh.edu.pl.diet.entities.DietaryProgramme;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class DailyMenuResponse {

    private Long dailyMenuId;
    private String dailyMenuName;
    private String dailyMenuDate;
    private Integer mealsQuantity;
    private List<String> meals;

    public DailyMenuResponse() {
        this.meals = new ArrayList<>();
    }

    public Long getDailyMenuId() {
        return dailyMenuId;
    }

    public void setDailyMenuId(Long dailyMenuId) {
        this.dailyMenuId = dailyMenuId;
    }

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

    public void addMeal(String meal) {
        this.meals.add(meal);
    }
}
