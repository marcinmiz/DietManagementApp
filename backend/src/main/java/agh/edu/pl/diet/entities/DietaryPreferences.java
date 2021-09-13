package agh.edu.pl.diet.entities;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Dietary_preferences")
public class DietaryPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dietaryPreferenceId;
//    private String dietaryPreferencesName;
    private Integer totalCalories;
    @ManyToOne
    private User preferenceOwner;
    @ManyToOne
    private DietType dietType;
    private Integer caloriesPerMeal;
    private Integer mealsQuantity;
    private Double targetWeight;

    @OneToMany(mappedBy = "dietaryPreferences", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Set<DietaryPreferencesNutrient> nutrients = new HashSet<>();

    @OneToMany(mappedBy = "dietaryPreferences", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Set<DietaryPreferencesProduct> products = new HashSet<>();

    @OneToMany(mappedBy = "dietaryPreferences", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Set<DietaryPreferencesRecipe> recipes = new HashSet<>();

    private String creationDate = null;

    public Long getDietaryPreferenceId() {
        return dietaryPreferenceId;
    }

    public void setDietaryPreferenceId(Long dietaryPreferenceId) {
        this.dietaryPreferenceId = dietaryPreferenceId;
    }

//    public String getDietaryPreferencesName() {
//        return dietaryPreferencesName;
//    }
//
//    public void setDietaryPreferencesName(String dietaryPreferencesName) {
//        this.dietaryPreferencesName = dietaryPreferencesName;
//    }

    public Integer getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(Integer totalCalories) {
        this.totalCalories = totalCalories;
    }

    public User getPreferenceOwner() {
        return preferenceOwner;
    }

    public void setPreferenceOwner(User preferenceOwner) {
        this.preferenceOwner = preferenceOwner;
    }

    public Set<DietaryPreferencesNutrient> getNutrients() {
        return nutrients;
    }

    public void addNutrient(DietaryPreferencesNutrient dietaryPreferencesNutrient) {
        nutrients.add(dietaryPreferencesNutrient);
    }

    public void setNutrients(Set<DietaryPreferencesNutrient> nutrients) {
        this.nutrients = nutrients;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(Double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public DietType getDietType() {
        return dietType;
    }

    public void setDietType(DietType dietType) {
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

    public Set<DietaryPreferencesProduct> getProducts() {
        return products;
    }

    public void setProducts(Set<DietaryPreferencesProduct> products) {
        this.products = products;
    }

    public Set<DietaryPreferencesRecipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<DietaryPreferencesRecipe> recipes) {
        this.recipes = recipes;
    }
}
