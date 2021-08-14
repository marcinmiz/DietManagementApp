package agh.edu.pl.diet.payloads.request;

import agh.edu.pl.diet.entities.User;

import javax.validation.constraints.NotBlank;

public class RecipeRequest {

    @NotBlank
    private String recipeName;
    @NotBlank
    private String product;
    @NotBlank
    private User owner;

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
