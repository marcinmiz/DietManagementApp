package agh.edu.pl.diet.entities;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Recipes")
public class Recipes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recipeId;
    private String recipeName;
    @ManyToOne
    private User recipeOwner;

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private List<RecipeProduct> recipeProducts = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private List<RecipeStep> recipeSteps = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private List<RecipeCustomerSatisfaction> recipeCustomerSatisfactions = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Set<DietaryPreferencesRecipe> dietaryPreferences = new HashSet<>();

    private Boolean recipeShared = false;

    private String creationDate = null;

    private String approvalStatus = "pending";
    private String assessmentDate = null;
    private String rejectExplanation = "";

    @Transient
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

    public User getRecipeOwner() {
        return recipeOwner;
    }

    public void setRecipeOwner(User recipeOwner) {
        this.recipeOwner = recipeOwner;
    }

    public List<RecipeProduct> getRecipeProducts() {
        return recipeProducts;
    }

    public void setRecipeProducts(List<RecipeProduct> recipeProducts) {
        this.recipeProducts = recipeProducts;
    }

    public void addRecipeProduct(RecipeProduct recipeProduct) {
        this.recipeProducts.add(recipeProduct);
    }

    public void setRecipeImage(String url) {
        this.recipeImage = url;
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

    public Boolean getRecipeShared() {
        return recipeShared;
    }

    public void setRecipeShared(Boolean recipeShared) {
        this.recipeShared = recipeShared;
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

    public List<RecipeStep> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(List<RecipeStep> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public void addRecipeStep(RecipeStep recipeStep) {
        recipeSteps.add(recipeStep);
    }

    public List<RecipeCustomerSatisfaction> getRecipeCustomerSatisfactions() {
        return recipeCustomerSatisfactions;
    }

    public void setRecipeCustomerSatisfactions(List<RecipeCustomerSatisfaction> recipeCustomerSatisfactions) {
        this.recipeCustomerSatisfactions = recipeCustomerSatisfactions;
    }

    public void addRecipeCustomerSatisfaction(RecipeCustomerSatisfaction recipeCustomerSatisfaction) {
        recipeCustomerSatisfactions.add(recipeCustomerSatisfaction);
    }
}
