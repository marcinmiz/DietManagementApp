package agh.edu.pl.diet.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Recipe_steps")
public class RecipeStep {
    @JsonIgnore
    @Id
    @Column(name = "recipe_step_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recipeStepId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinColumn(name="recipe_id")
    private Recipes recipe;

    @NotNull
    private String recipeStepDescription;

    public RecipeStep() {
    }

    public RecipeStep(Recipes recipe, @NotNull String recipeStepDescription) {
        this.recipe = recipe;
        this.recipeStepDescription = recipeStepDescription;
    }

    public Long getRecipeStepId() {
        return recipeStepId;
    }

    public void setRecipeStepId(Long recipeStepId) {
        this.recipeStepId = recipeStepId;
    }

    public Recipes getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipes recipe) {
        this.recipe = recipe;
    }

    public void removeRecipe(Recipes recipe) {
        this.recipe.removeRecipeStep(this);
        this.recipe = null;
    }

    public String getRecipeStepDescription() {
        return recipeStepDescription;
    }

    public void setRecipeStepDescription(String recipeStepDescription) {
        this.recipeStepDescription = recipeStepDescription;
    }
}
