package agh.edu.pl.diet.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Recipe_customer_satisfactions")
public class RecipeCustomerSatisfaction {
    @JsonIgnore
    @Id
    @Column(name = "recipeCustomerSatisfactionId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recipeCustomerSatisfactionId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinColumn(name="recipeId", nullable=false)
    private Recipes recipe;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinColumn(name="userId", nullable=false)
    private User customerSatisfactionOwner;

    private Float recipeRating;

    @NotNull
    private Boolean recipeFavourite;

    public RecipeCustomerSatisfaction() {
    }

    public RecipeCustomerSatisfaction(Recipes recipe, User customerSatisfactionOwner) {
        this.recipe = recipe;
        this.customerSatisfactionOwner = customerSatisfactionOwner;
        this.recipeRating = null;
        this.recipeFavourite = false;
    }

    public Long getRecipeCustomerSatisfactionId() {
        return recipeCustomerSatisfactionId;
    }

    public void setRecipeCustomerSatisfactionId(Long recipeCustomerSatisfactionId) {
        this.recipeCustomerSatisfactionId = recipeCustomerSatisfactionId;
    }

    public Recipes getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipes recipe) {
        this.recipe = recipe;
    }

    public User getCustomerSatisfactionOwner() {
        return customerSatisfactionOwner;
    }

    public void setCustomerSatisfactionOwner(User customerSatisfactionOwner) {
        this.customerSatisfactionOwner = customerSatisfactionOwner;
    }

    public Float getRecipeRating() {
        return recipeRating;
    }

    public void setRecipeRating(Float recipeRating) {
        this.recipeRating = recipeRating;
    }

    public Boolean getRecipeFavourite() {
        return recipeFavourite;
    }

    public void setRecipeFavourite(Boolean recipeFavourite) {
        this.recipeFavourite = recipeFavourite;
    }
}
