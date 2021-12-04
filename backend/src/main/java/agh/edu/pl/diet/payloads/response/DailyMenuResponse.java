package agh.edu.pl.diet.payloads.response;

import agh.edu.pl.diet.entities.RecipeProduct;
import agh.edu.pl.diet.entities.RecipeStep;

import java.util.ArrayList;
import java.util.List;

public class DailyMenuResponse {

    private Long dailyMenuId;
    private String dailyMenuName;
    private String dailyMenuDate;
    private Integer mealsQuantity;
    private List<Meal> meals;

    public DailyMenuResponse(Long dailyMenuId, String dailyMenuName, String dailyMenuDate, Integer mealsQuantity) {
        this.dailyMenuId = dailyMenuId;
        this.dailyMenuName = dailyMenuName;
        this.dailyMenuDate = dailyMenuDate;
        this.mealsQuantity = mealsQuantity;
        this.meals = new ArrayList<>();
    }

    private class Meal {
        private Long mealId;
        private String mealName;
        private String mealHourTime;
        private Recipe recipe;
        private Boolean consumed;

        private Meal(Long mealId, String mealName, String mealHourTime, Recipe recipe, Boolean consumed) {
            this.mealId = mealId;
            this.mealName = mealName;
            this.mealHourTime = mealHourTime;
            this.recipe = recipe;
            this.consumed = consumed;
        }

        public String getMealName() {
            return mealName;
        }

        public String getMealHourTime() {
            return mealHourTime;
        }

        public Recipe getRecipe() {
            return recipe;
        }

        public Long getMealId() {
            return mealId;
        }

        public Boolean getConsumed() {
            return consumed;
        }
    }

    public class Recipe {
        private Long recipeId;
        private String recipeName;
        private String creationDate;
        private Long recipeAuthorId;
        private String recipeAuthor;
        private String recipeAuthorImage;
        private String recipeImage;
        private Double recipeCalories;
        private Double recipeProteins;
        private Double recipeCarbohydrates;
        private Double recipeFats;
        private String inCollection;
        private String likedInPreference;
        private String favourite;
        private List<RecipeProduct> recipeIngredients;
        private List<RecipeStep> recipeSteps;

        private Recipe(Long recipeId, String recipeName, String creationDate, Long recipeAuthorId, String recipeAuthor, String recipeAuthorImage, String recipeImage, Double recipeCalories, Double recipeProteins, Double recipeCarbohydrates, Double recipeFats, String inCollection, String likedInPreference, String favourite, List<RecipeProduct> recipeIngredients, List<RecipeStep> recipeSteps) {
            this.recipeId = recipeId;
            this.recipeName = recipeName;
            this.creationDate = creationDate;
            this.recipeAuthorId = recipeAuthorId;
            this.recipeAuthor = recipeAuthor;
            this.recipeAuthorImage = recipeAuthorImage;
            this.recipeImage = recipeImage;
            this.recipeCalories = recipeCalories;
            this.recipeProteins = recipeProteins;
            this.recipeCarbohydrates = recipeCarbohydrates;
            this.recipeFats = recipeFats;
            this.inCollection = inCollection;
            this.likedInPreference = likedInPreference;
            this.favourite = favourite;
            this.recipeIngredients = recipeIngredients;
            this.recipeSteps = recipeSteps;
        }

        public Long getRecipeId() {
            return recipeId;
        }

        public String getRecipeName() {
            return recipeName;
        }

        public String getCreationDate() {
            return creationDate;
        }

        public Long getRecipeAuthorId() {
            return recipeAuthorId;
        }

        public String getRecipeAuthor() {
            return recipeAuthor;
        }

        public String getRecipeAuthorImage() {
            return recipeAuthorImage;
        }

        public String getRecipeImage() {
            return recipeImage;
        }

        public Double getRecipeCalories() {
            return recipeCalories;
        }

        public Double getRecipeProteins() {
            return recipeProteins;
        }

        public Double getRecipeCarbohydrates() {
            return recipeCarbohydrates;
        }

        public Double getRecipeFats() {
            return recipeFats;
        }

        public String getInCollection() {
            return inCollection;
        }

        public String getLikedInPreference() {
            return likedInPreference;
        }

        public String getFavourite() {
            return favourite;
        }

        public List<RecipeProduct> getRecipeIngredients() {
            return recipeIngredients;
        }

        public List<RecipeStep> getRecipeSteps() {
            return recipeSteps;
        }
    }

    public Long getDailyMenuId() {
        return dailyMenuId;
    }

    public String getDailyMenuName() {
        return dailyMenuName;
    }

    public String getDailyMenuDate() {
        return dailyMenuDate;
    }

    public Integer getMealsQuantity() {
        return mealsQuantity;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void addMeal(Long mealId, String mealName, String mealHourTime, Recipe recipe, Boolean consumed) {
        this.meals.add(new Meal(mealId, mealName, mealHourTime, recipe, consumed));
    }

    public Recipe createRecipe(Long recipeId, String recipeName, String creationDate, Long recipeAuthorId, String recipeAuthor, String recipeAuthorImage, String recipeImage, Double recipeCalories, Double recipeProteins, Double recipeCarbohydrates, Double recipeFats, String inCollection, String likedInPreference, String favourite, List<RecipeProduct> recipeIngredients, List<RecipeStep> recipeSteps) {

        return new Recipe(recipeId, recipeName, creationDate, recipeAuthorId, recipeAuthor, recipeAuthorImage, recipeImage, recipeCalories, recipeProteins, recipeCarbohydrates, recipeFats, inCollection, likedInPreference, favourite, recipeIngredients, recipeSteps);
    }

}
