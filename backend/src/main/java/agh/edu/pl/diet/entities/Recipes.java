package agh.edu.pl.diet.entities;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Recipes")
public class Recipes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recipeId;
    private String recipeName;
    //	private String publicationDate;
    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Set<RecipeProduct> recipes = new HashSet<>();

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Set<DietaryPreferencesRecipe> dietaryPreferences = new HashSet<>();

    @Transient
    private String recipeImage;

    private String creationDate = null;


    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<RecipeProduct> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<RecipeProduct> recipes) {
        this.recipes = recipes;
    }

    public void setRecipeImage(String url) {
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Set<DietaryPreferencesRecipe> getDietaryPreferences() {
        return dietaryPreferences;
    }

    public void setDietaryPreferences(Set<DietaryPreferencesRecipe> dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }
}
