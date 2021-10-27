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
    private Double totalDailyCalories;
    @ManyToOne
    private User preferenceOwner;
    @ManyToOne
    private DietType dietType;
    private Integer caloriesPerMeal;
    private Integer mealsQuantity;
    private Double targetWeight;
    @OneToOne
    private DietaryProgramme relatedDietaryProgramme = null;

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

    public Double getTotalDailyCalories() {
        return totalDailyCalories;
    }

    public void setTotalDailyCalories(Double totalDailyCalories) {
        this.totalDailyCalories = totalDailyCalories;
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

    public void addProduct(DietaryPreferencesProduct product) {
        this.products.add(product);
    }

    public void removeProduct(DietaryPreferencesProduct product) {
        products.remove(product);
    }

    public Set<DietaryPreferencesRecipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<DietaryPreferencesRecipe> recipes) {
        this.recipes = recipes;
    }

    public void addRecipe(DietaryPreferencesRecipe recipe) {
        this.recipes.add(recipe);
    }

    public void removeRecipe(DietaryPreferencesRecipe recipe) {
        recipes.remove(recipe);
    }

    public DietaryProgramme getRelatedDietaryProgramme() {
        return relatedDietaryProgramme;
    }

    public void setRelatedDietaryProgramme(DietaryProgramme relatedDietaryProgramme) {
        this.relatedDietaryProgramme = relatedDietaryProgramme;
    }
}
