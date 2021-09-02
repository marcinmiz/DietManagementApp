package agh.edu.pl.diet.entities;

import javax.persistence.*;

@Entity
@Table(name = "DailyMenu")
public class DailyMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dailyMenuId;
    private String dailyMenuName;
    private String dailyMenuDate = null;
    private Integer mealsQuantity;
    @ManyToOne
    private DietaryProgramme dietaryProgramme;
    @ManyToOne
    private Meals meal;
    private String creationDate = null;

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

    public DietaryProgramme getDietaryProgramme() {
        return dietaryProgramme;
    }

    public void setDietaryProgramme(DietaryProgramme dietaryProgramme) {
        this.dietaryProgramme = dietaryProgramme;
    }

    public Meals getMeal() {
        return meal;
    }

    public void setMeal(Meals meal) {
        this.meal = meal;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
