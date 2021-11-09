package agh.edu.pl.diet.entities;

import javax.persistence.*;

@Entity
@Table(name = "Meals")
public class Meals {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mealId;
    private String mealsName;
    private String mealHourTime = null;
    @ManyToOne
    private DailyMenu dailyMenu;
    @ManyToOne
    private Recipes recipe;
    private Boolean consumed = false;

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public String getMealsName() {
        return mealsName;
    }

    public void setMealsName(String mealsName) {
        this.mealsName = mealsName;
    }

    public String getMealHourTime() {
        return mealHourTime;
    }

    public void setMealHourTime(String mealHourTime) {
        this.mealHourTime = mealHourTime;
    }

    public DailyMenu getDailyMenu() {
        return dailyMenu;
    }

    public void setDailyMenu(DailyMenu dailyMenu) {
        this.dailyMenu = dailyMenu;
    }

    public Recipes getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipes recipe) {
        this.recipe = recipe;
    }

    public Boolean getConsumed() {
        return consumed;
    }

    public void setConsumed(Boolean consumed) {
        this.consumed = consumed;
    }
}
