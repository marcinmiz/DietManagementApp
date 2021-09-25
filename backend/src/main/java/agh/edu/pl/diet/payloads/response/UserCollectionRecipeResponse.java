package agh.edu.pl.diet.payloads.response;

import agh.edu.pl.diet.entities.RecipeCustomerSatisfaction;
import agh.edu.pl.diet.entities.RecipeStep;

import java.util.ArrayList;
import java.util.List;

public class UserCollectionRecipeResponse {
    private Long recipeId;
    private String recipeName;
    private UserResponse recipeOwner;

    private List<RecipeProductResponse> recipeProducts = new ArrayList<>();

    private List<RecipeStep> recipeSteps = new ArrayList<>();

    private List<RecipeCustomerSatisfactionResponse> recipeCustomerSatisfactions = new ArrayList<>();

    private Boolean recipeShared = false;

    private String creationDate = null;

    private String approvalStatus = "pending";
    private String assessmentDate = null;
    private String rejectExplanation = "";

    private String recipeImage = "";

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

    public UserResponse getRecipeOwner() {
        return recipeOwner;
    }

    public void setRecipeOwner(UserResponse recipeOwner) {
        this.recipeOwner = recipeOwner;
    }

    public List<RecipeProductResponse> getRecipeProducts() {
        return recipeProducts;
    }

    public void setRecipeProducts(List<RecipeProductResponse> recipeProducts) {
        this.recipeProducts = recipeProducts;
    }

    public List<RecipeStep> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(List<RecipeStep> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public List<RecipeCustomerSatisfactionResponse> getRecipeCustomerSatisfactions() {
        return recipeCustomerSatisfactions;
    }

    public void setRecipeCustomerSatisfactions(List<RecipeCustomerSatisfactionResponse> recipeCustomerSatisfactions) {
        this.recipeCustomerSatisfactions = recipeCustomerSatisfactions;
    }

    public Boolean getRecipeShared() {
        return recipeShared;
    }

    public void setRecipeShared(Boolean recipeShared) {
        this.recipeShared = recipeShared;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(String assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public String getRejectExplanation() {
        return rejectExplanation;
    }

    public void setRejectExplanation(String rejectExplanation) {
        this.rejectExplanation = rejectExplanation;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }
}
