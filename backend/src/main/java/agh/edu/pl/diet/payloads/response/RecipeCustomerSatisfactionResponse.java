package agh.edu.pl.diet.payloads.response;

public class RecipeCustomerSatisfactionResponse {
    private Long recipeCustomerSatisfactionId;

    private UserResponse customerSatisfactionOwner;

    private Float recipeRating;

    private Boolean recipeFavourite;

    public Long getRecipeCustomerSatisfactionId() {
        return recipeCustomerSatisfactionId;
    }

    public void setRecipeCustomerSatisfactionId(Long recipeCustomerSatisfactionId) {
        this.recipeCustomerSatisfactionId = recipeCustomerSatisfactionId;
    }

    public UserResponse getCustomerSatisfactionOwner() {
        return customerSatisfactionOwner;
    }

    public void setCustomerSatisfactionOwner(UserResponse customerSatisfactionOwner) {
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
