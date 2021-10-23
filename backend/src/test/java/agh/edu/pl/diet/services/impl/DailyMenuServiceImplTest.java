package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.*;
import agh.edu.pl.diet.payloads.request.RecipeGetRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.DailyMenuRepo;
import agh.edu.pl.diet.services.DailyMenuService;
import agh.edu.pl.diet.services.MealService;
import agh.edu.pl.diet.services.RecipeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class DailyMenuServiceImplTest {

    private List<Category> categories = List.of(new Category("Fruit"), new Category("Vegetables"), new Category("Cereal"), new Category("Dairy"), new Category("Meat"), new Category("Fish"), new Category("Fats"), new Category("Sweets"), new Category("Nuts"));
    @Mock
    private RecipeService recipeService;
    @Mock
    private DailyMenuRepo dailyMenuRepo;
    @Mock
    private MealService mealService;
    @InjectMocks
    private DailyMenuService dailyMenuService = new DailyMenuServiceImpl();
    @InjectMocks
    private DailyMenuService spyDailyMenuService = Mockito.spy(dailyMenuService);
    private Map<String, List<Double>> nutrientsScopes;
    private Map<String, List<Double>> dailyNutrientsScopes;
    private Map<String, Double> totalDailyNutrients;
    private List<Nutrient> nutrients;
    private List<List<ProductNutrient>> productNutrients;
    private List<Product> products;
    private List<RecipeProduct> recipeProducts = new ArrayList<>();
    private List<RecipeCustomerSatisfaction> satisfactions;
    private List<Recipes> collection;
    private List<Recipes> allRecipes;

    @BeforeEach
    void setUp() {
        nutrientsScopes = new LinkedHashMap<>();
        totalDailyNutrients = new LinkedHashMap<>();
        dailyNutrientsScopes = new LinkedHashMap<>();

        String mealName = "breakfast";

        nutrientsScopes.put("breakfast", List.of(0.25, 0.3));
        nutrientsScopes.put("lunch", List.of(0.05, 0.1));
        nutrientsScopes.put("dinner", List.of(0.35, 0.4));
        nutrientsScopes.put("supper", List.of(0.25, 0.3));

        totalDailyNutrients.put("Protein", 120.0);
        totalDailyNutrients.put("Carbohydrate", 80.0);
        totalDailyNutrients.put("Fat", 40.0);

        dailyNutrientsScopes.put("Protein", List.of(nutrientsScopes.get(mealName).get(0) * totalDailyNutrients.get("Protein"), nutrientsScopes.get(mealName).get(1) * totalDailyNutrients.get("Protein")));
        dailyNutrientsScopes.put("Carbohydrate", List.of(nutrientsScopes.get(mealName).get(0) * totalDailyNutrients.get("Carbohydrate"), nutrientsScopes.get(mealName).get(1) * totalDailyNutrients.get("Carbohydrate")));
        dailyNutrientsScopes.put("Fat", List.of(nutrientsScopes.get(mealName).get(0) * totalDailyNutrients.get("Fat"), nutrientsScopes.get(mealName).get(1) * totalDailyNutrients.get("Fat")));

        Recipes recipe1 = new Recipes();
        recipe1.setRecipeId(700L);
        Recipes recipe2 = new Recipes();
        recipe2.setRecipeId(705L);
        Recipes recipe3 = new Recipes();
        recipe3.setRecipeId(712L);
        Recipes recipe4 = new Recipes();
        recipe4.setRecipeId(720L);
        collection = List.of(recipe1, recipe2, recipe3, recipe4);

        nutrients = new ArrayList<>();
        Nutrient nutrient = new Nutrient();
        nutrient.setNutrientName("Protein");
        nutrients.add(nutrient);
        Nutrient nutrient2 = new Nutrient();
        nutrient2.setNutrientName("Carbohydrate");
        nutrients.add(nutrient2);
        Nutrient nutrient3 = new Nutrient();
        nutrient3.setNutrientName("Fat");
        nutrients.add(nutrient3);

        productNutrients = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            List<ProductNutrient> oneProduct = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ProductNutrient productNutrient = new ProductNutrient();
                productNutrient.setNutrient(nutrients.get(j));
                oneProduct.add(productNutrient);
            }
            productNutrients.add(oneProduct);
        }

        products = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Product product = new Product();
            products.add(product);
        }

        for (Product currentProduct : products) {
            RecipeProduct recipeProduct = new RecipeProduct();
            recipeProducts.add(recipeProduct);
        }

        satisfactions = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            RecipeCustomerSatisfaction satisfaction = new RecipeCustomerSatisfaction();
            satisfactions.add(satisfaction);
        }

        //initialization for addNewDailyMenu() method

        allRecipes = new ArrayList<>();

        Double[][] nutrientsAmounts = new Double[3][3];
        Double[] nutrient4 = {12.0, 30.4, 5.5};
        nutrientsAmounts[0] = nutrient4;
        Double[] nutrient5 = {11.6, 24.0, 10.7};
        nutrientsAmounts[1] = nutrient5;
        Double[] nutrient6 = {6.1, 24.8, 8.55};
        nutrientsAmounts[2] = nutrient6;
        Double[] calories = {216.0, 123.0, 262.0};

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {
                productNutrients.get(i).get(j).setNutrientAmount(nutrientsAmounts[i][j]);
            }
            products.get(i).setCalories(calories[i]);
            products.get(i).setNutrients(productNutrients.get(i));
        }

        for (int i = 0; i < recipeProducts.size(); i++) {
            recipeProducts.get(i).setProduct(products.get(i));
        }

        Recipes recipe = new Recipes();
        recipe.setRecipeId(704L);
        recipe.setRecipeName("spaghetti");
        recipe.setRecipeProducts(recipeProducts);

        allRecipes.add(recipe);

        productNutrients = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            List<ProductNutrient> oneProduct = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ProductNutrient productNutrient = new ProductNutrient();
                productNutrient.setNutrient(nutrients.get(j));
                oneProduct.add(productNutrient);
            }
            productNutrients.add(oneProduct);
        }

        products = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Product product = new Product();
            products.add(product);
        }
        recipeProducts = new ArrayList<>();
        for (Product currentProduct : products) {
            RecipeProduct recipeProduct = new RecipeProduct();
            recipeProducts.add(recipeProduct);
        }

        nutrient4 = new Double[]{3.0, 12.4, 2.5};
        nutrientsAmounts[0] = nutrient4;
        nutrient5 = new Double[]{2.1, 4.5, 1.25};
        nutrientsAmounts[1] = nutrient5;
        nutrient6 = new Double[]{3.0, 4.7, 3.0};
        nutrientsAmounts[2] = nutrient6;
        calories = new Double[]{64.5, 50.0, 65.05};

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {
                productNutrients.get(i).get(j).setNutrientAmount(nutrientsAmounts[i][j]);
            }
            products.get(i).setCalories(calories[i]);
            products.get(i).setNutrients(productNutrients.get(i));
        }

        for (int i = 0; i < 3; i++) {
            recipeProducts.get(i).setProduct(products.get(i));
        }

        recipe = new Recipes();
        recipe.setRecipeId(705L);
        recipe.setRecipeName("risotto");
        recipe.setRecipeProducts(recipeProducts);

        allRecipes.add(recipe);

        productNutrients = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            List<ProductNutrient> oneProduct = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ProductNutrient productNutrient = new ProductNutrient();
                productNutrient.setNutrient(nutrients.get(j));
                oneProduct.add(productNutrient);
            }
            productNutrients.add(oneProduct);
        }

        products = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Product product = new Product();
            products.add(product);
        }
        recipeProducts = new ArrayList<>();
        for (Product currentProduct : products) {
            RecipeProduct recipeProduct = new RecipeProduct();
            recipeProducts.add(recipeProduct);
        }

        nutrient4 = new Double[]{14.0, 23.88, 10.8};
        nutrientsAmounts[0] = nutrient4;
        nutrient5 = new Double[]{16.5, 54.0, 14.4};
        nutrientsAmounts[1] = nutrient5;
        nutrient6 = new Double[]{18.1, 25.8, 8.55};
        nutrientsAmounts[2] = nutrient6;
        calories = new Double[]{345.0, 289.0, 251.0};

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {
                productNutrients.get(i).get(j).setNutrientAmount(nutrientsAmounts[i][j]);
            }

            products.get(i).setCalories(calories[i]);
            products.get(i).setNutrients(productNutrients.get(i));
        }

        for (int i = 0; i < 3; i++) {
            recipeProducts.get(i).setProduct(products.get(i));
        }

        recipe = new Recipes();
        recipe.setRecipeId(706L);
        recipe.setRecipeName("guacamole");
        recipe.setRecipeProducts(recipeProducts);

        allRecipes.add(recipe);

        productNutrients = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            List<ProductNutrient> oneProduct = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ProductNutrient productNutrient = new ProductNutrient();
                productNutrient.setNutrient(nutrients.get(j));
                oneProduct.add(productNutrient);
            }
            productNutrients.add(oneProduct);
        }

        products = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Product product = new Product();
            products.add(product);
        }
        recipeProducts = new ArrayList<>();
        for (Product currentProduct : products) {
            RecipeProduct recipeProduct = new RecipeProduct();
            recipeProducts.add(recipeProduct);
        }

        nutrient4 = new Double[]{13.0, 20.3, 10.7};
        nutrientsAmounts[0] = nutrient4;
        nutrient5 = new Double[]{8.7, 30.2, 6.2};
        nutrientsAmounts[1] = nutrient5;
        nutrient6 = new Double[]{8.0, 33.5, 7.85};
        nutrientsAmounts[2] = nutrient6;
        calories = new Double[]{248.0, 312.0, 114.0};

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {
                productNutrients.get(i).get(j).setNutrientAmount(nutrientsAmounts[i][j]);
            }

            products.get(i).setCalories(calories[i]);
            products.get(i).setNutrients(productNutrients.get(i));
        }

        for (int i = 0; i < 3; i++) {
            recipeProducts.get(i).setProduct(products.get(i));
        }

        recipe = new Recipes();
        recipe.setRecipeId(707L);
        recipe.setRecipeName("paella");
        recipe.setRecipeProducts(recipeProducts);

        allRecipes.add(recipe);

        productNutrients = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            List<ProductNutrient> oneProduct = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ProductNutrient productNutrient = new ProductNutrient();
                productNutrient.setNutrient(nutrients.get(j));
                oneProduct.add(productNutrient);
            }
            productNutrients.add(oneProduct);
        }

        products = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Product product = new Product();
            products.add(product);
        }
        recipeProducts = new ArrayList<>();
        for (Product currentProduct : products) {
            RecipeProduct recipeProduct = new RecipeProduct();
            recipeProducts.add(recipeProduct);
        }

        nutrient4 = new Double[]{3.0, 12.4, 2.5};
        nutrientsAmounts[0] = nutrient4;
        nutrient5 = new Double[]{2.1, 4.5, 1.25};
        nutrientsAmounts[1] = nutrient5;
        nutrient6 = new Double[]{3.0, 4.7, 3.0};
        nutrientsAmounts[2] = nutrient6;
        calories = new Double[]{65.0, 109.0, 65.0};

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {
                productNutrients.get(i).get(j).setNutrientAmount(nutrientsAmounts[i][j]);
            }
            products.get(i).setCalories(calories[i]);
            products.get(i).setNutrients(productNutrients.get(i));
        }

        for (int i = 0; i < 3; i++) {
            recipeProducts.get(i).setProduct(products.get(i));
        }

        recipe = new Recipes();
        recipe.setRecipeId(708L);
        recipe.setRecipeName("pizza");
        recipe.setRecipeProducts(recipeProducts);

        allRecipes.add(recipe);

//        Mockito.when(recipeService.getRecipes(any(RecipeGetRequest.class))).thenReturn(collection);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldRecipeBeAppropriate() {
        String expected = "Recipe is appropriate in regard to dietary preference";

        Double[][] nutrientsAmounts = new Double[3][3];
        Double[] nutrient1 = {12.0, 14.4, 5.5};
        nutrientsAmounts[0] = nutrient1;
        Double[] nutrient2 = {11.6, 4.0, 2.7};
        nutrientsAmounts[1] = nutrient2;
        Double[] nutrient3 = {10.0, 4.0, 3.0};
        nutrientsAmounts[2] = nutrient3;
        Integer[] categoryIndexes = {1, 2, 5};
        Float[] ratings = {4.7F, 4.2F};

        for (int i = 0; i < products.size(); i++) {

            products.get(i).setCategory(categories.get(categoryIndexes[i]));

            for (int j = 0; j < 3; j++) {
                productNutrients.get(i).get(j).setNutrientAmount(nutrientsAmounts[i][j]);
            }
            products.get(i).setNutrients(productNutrients.get(i));
        }

        for (int i = 0; i < recipeProducts.size(); i++) {
            recipeProducts.get(i).setProduct(products.get(i));
        }
        for (int i = 0; i < satisfactions.size(); i++) {
            satisfactions.get(i).setRecipeRating(ratings[i]);
        }

        Recipes recipe = new Recipes();
        recipe.setRecipeId(704L);
        recipe.setRecipeName("spaghetti");
        recipe.setRecipeProducts(recipeProducts);
        recipe.setRecipeCustomerSatisfactions(satisfactions);

        doReturn(collection).when(recipeService).getRecipes(any(RecipeGetRequest.class));

        ResponseMessage actual = dailyMenuService.verifyRecipe(recipe, dailyNutrientsScopes);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldRecipeBeInappropriateDueToAmountNutrient() {
        String expected = "Recipe has inappropriate amount of Fat";

        Double[][] nutrientsAmounts = new Double[3][3];
        Double[] nutrient1 = {12.0, 14.4, 5.5};
        nutrientsAmounts[0] = nutrient1;
        Double[] nutrient2 = {11.6, 4.0, 2.7};
        nutrientsAmounts[1] = nutrient2;
        Double[] nutrient3 = {10.0, 4.0, 3.85};
        nutrientsAmounts[2] = nutrient3;
        Integer[] categoryIndexes = {1, 2, 5};
        Float[] ratings = {4.7F, 4.2F};

        for (int i = 0; i < products.size(); i++) {

            products.get(i).setCategory(categories.get(categoryIndexes[i]));

            for (int j = 0; j < 3; j++) {
                productNutrients.get(i).get(j).setNutrientAmount(nutrientsAmounts[i][j]);
            }
            products.get(i).setNutrients(productNutrients.get(i));
        }

        for (int i = 0; i < recipeProducts.size(); i++) {
            recipeProducts.get(i).setProduct(products.get(i));
        }
        for (int i = 0; i < satisfactions.size(); i++) {
            satisfactions.get(i).setRecipeRating(ratings[i]);
        }

        Recipes recipe = new Recipes();
        recipe.setRecipeId(704L);
        recipe.setRecipeName("spaghetti");
        recipe.setRecipeProducts(recipeProducts);
        recipe.setRecipeCustomerSatisfactions(satisfactions);

        ResponseMessage actual = dailyMenuService.verifyRecipe(recipe, dailyNutrientsScopes);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldRecipeBeInappropriateDueToProductCategories() {
        String expected = "Recipe is inappropriate in regard to dietary preference";

        Double[][] nutrientsAmounts = new Double[3][3];
        Double[] nutrient1 = {12.0, 14.4, 5.5};
        nutrientsAmounts[0] = nutrient1;
        Double[] nutrient2 = {11.6, 4.0, 2.7};
        nutrientsAmounts[1] = nutrient2;
        Double[] nutrient3 = {10.0, 4.0, 3.0};
        nutrientsAmounts[2] = nutrient3;
        Integer[] categoryIndexes = {1, 5, 5};
        Float[] ratings = {4.7F, 4.2F};

        for (int i = 0; i < products.size(); i++) {

            products.get(i).setCategory(categories.get(categoryIndexes[i]));

            for (int j = 0; j < 3; j++) {
                productNutrients.get(i).get(j).setNutrientAmount(nutrientsAmounts[i][j]);
            }
            products.get(i).setNutrients(productNutrients.get(i));
        }

        for (int i = 0; i < recipeProducts.size(); i++) {
            recipeProducts.get(i).setProduct(products.get(i));
        }
        for (int i = 0; i < satisfactions.size(); i++) {
            satisfactions.get(i).setRecipeRating(ratings[i]);
        }

        Recipes recipe = new Recipes();
        recipe.setRecipeId(704L);
        recipe.setRecipeName("spaghetti");
        recipe.setRecipeProducts(recipeProducts);
        recipe.setRecipeCustomerSatisfactions(satisfactions);

        doReturn(collection).when(recipeService).getRecipes(any(RecipeGetRequest.class));

        ResponseMessage actual = dailyMenuService.verifyRecipe(recipe, dailyNutrientsScopes);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldRecipeBeInappropriateDueToLowRatings() {
        String expected = "Recipe is inappropriate in regard to dietary preference";

        Double[][] nutrientsAmounts = new Double[3][3];
        Double[] nutrient1 = {12.0, 14.4, 5.5};
        nutrientsAmounts[0] = nutrient1;
        Double[] nutrient2 = {11.6, 4.0, 2.7};
        nutrientsAmounts[1] = nutrient2;
        Double[] nutrient3 = {10.0, 4.0, 3.0};
        nutrientsAmounts[2] = nutrient3;
        Integer[] categoryIndexes = {1, 2, 5};
        Float[] ratings = {2.7F, 3.2F};

        for (int i = 0; i < products.size(); i++) {

            products.get(i).setCategory(categories.get(categoryIndexes[i]));

            for (int j = 0; j < 3; j++) {
                productNutrients.get(i).get(j).setNutrientAmount(nutrientsAmounts[i][j]);
            }
            products.get(i).setNutrients(productNutrients.get(i));
        }

        for (int i = 0; i < recipeProducts.size(); i++) {
            recipeProducts.get(i).setProduct(products.get(i));
        }
        for (int i = 0; i < satisfactions.size(); i++) {
            satisfactions.get(i).setRecipeRating(ratings[i]);
        }

        Recipes recipe = new Recipes();
        recipe.setRecipeId(704L);
        recipe.setRecipeName("spaghetti");
        recipe.setRecipeProducts(recipeProducts);
        recipe.setRecipeCustomerSatisfactions(satisfactions);

        doReturn(collection).when(recipeService).getRecipes(any(RecipeGetRequest.class));

        ResponseMessage actual = dailyMenuService.verifyRecipe(recipe, dailyNutrientsScopes);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldAddDailyMenu() {
        System.out.println();
        System.out.println("shouldAddDailyMenu");
        System.out.println();

        //pizza compensates too little calories of spaghetti and paella compensates too little calories of guacamole

        String expected = "Daily Menu has been added";
        DietaryProgramme dietaryProgramme = new DietaryProgramme();
        Double totalDailyCalories = 2394.0;
        Integer mealsQuantity = 4;
        Calendar startDate = Calendar.getInstance();
        Integer currentDay = 3, lastDay = 14;

        totalDailyNutrients.clear();
        totalDailyNutrients.put("Protein", 108.0);
        totalDailyNutrients.put("Carbohydrate", 288.0);
        totalDailyNutrients.put("Fat", 90.0);

        for (Recipes qrecipe : allRecipes) {

            System.out.println(qrecipe.getRecipeName());
            System.out.println(qrecipe.getRecipeCalories());
        }

        DailyMenu dailyMenu = new DailyMenu();
        dailyMenu.setDailyMenuName("Day 3 of 14");

        doReturn(allRecipes).when(recipeService).getRecipes(any(RecipeGetRequest.class));

        doReturn(new ResponseMessage("Meal has been added")).when(mealService).addNewMeal(anyString(), any(Recipes.class), any(DailyMenu.class));

        doReturn(dailyMenu).when(dailyMenuRepo).save(any(DailyMenu.class));

        doReturn(new ResponseMessage("Recipe is appropriate in regard to dietary preference")).when(spyDailyMenuService).verifyRecipe(any(Recipes.class), anyMap());

        ResponseMessage actual = spyDailyMenuService.addNewDailyMenu(dietaryProgramme, totalDailyCalories, mealsQuantity, totalDailyNutrients, currentDay, lastDay);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldFailDueToRecipeCaloriesMatch() {
        System.out.println();
        System.out.println("shouldFailDueToRecipeCaloriesMatch");
        System.out.println();

        String expected = "No recipe is appropriate for lunch";
        DietaryProgramme dietaryProgramme = new DietaryProgramme();
        Double totalDailyCalories = 2394.0;
        Integer mealsQuantity = 4;
        Calendar startDate = Calendar.getInstance();
        Integer currentDay = 3, lastDay = 14;

        totalDailyNutrients.clear();
        totalDailyNutrients.put("Protein", 108.0);
        totalDailyNutrients.put("Carbohydrate", 288.0);
        totalDailyNutrients.put("Fat", 90.0);

        //remove pizza from allRecipes to create lack of dish for lunch
        allRecipes.remove(4);

        for (Recipes qrecipe : allRecipes) {

            System.out.println(qrecipe.getRecipeName());
            System.out.println(qrecipe.getRecipeCalories());
        }

        doReturn(allRecipes).when(recipeService).getRecipes(any(RecipeGetRequest.class));

        doReturn(new ResponseMessage("Recipe is appropriate in regard to dietary preference")).when(spyDailyMenuService).verifyRecipe(any(Recipes.class), anyMap());

        ResponseMessage actual = spyDailyMenuService.addNewDailyMenu(dietaryProgramme, totalDailyCalories, mealsQuantity, totalDailyNutrients, currentDay, lastDay);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldFailDueToRecipeProteinsMatch() {
        System.out.println();
        System.out.println("shouldFailDueToRecipeProteinsMatch");
        System.out.println();

        String expected = "No recipe is appropriate for dinner";
        DietaryProgramme dietaryProgramme = new DietaryProgramme();
        Double totalDailyCalories = 2394.0;
        Integer mealsQuantity = 4;
        Calendar startDate = Calendar.getInstance();
        Integer currentDay = 3, lastDay = 14;

        totalDailyNutrients.clear();
        totalDailyNutrients.put("Protein", 108.0);
        totalDailyNutrients.put("Carbohydrate", 288.0);
        totalDailyNutrients.put("Fat", 90.0);

        for (Recipes qrecipe : allRecipes) {

            System.out.println(qrecipe.getRecipeName());
            System.out.println(qrecipe.getRecipeCalories());
        }

        doReturn(allRecipes).when(recipeService).getRecipes(any(RecipeGetRequest.class));

        doReturn(new ResponseMessage("Recipe is appropriate in regard to dietary preference")).when(spyDailyMenuService).verifyRecipe(any(Recipes.class), anyMap());

        doReturn(new ResponseMessage("Recipe has inappropriate amount of Protein")).when(spyDailyMenuService).verifyRecipe(eq(allRecipes.get(2)), anyMap());

        ResponseMessage actual = spyDailyMenuService.addNewDailyMenu(dietaryProgramme, totalDailyCalories, mealsQuantity, totalDailyNutrients, currentDay, lastDay);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldFailDueToNotCreatedMeal() {
        System.out.println();
        System.out.println("shouldFailDueToNotCreatedMeal");
        System.out.println();

        //pizza compensates too little calories of spaghetti and paella compensates too little calories of guacamole

        String expected = "Meal has not been created";
        DietaryProgramme dietaryProgramme = new DietaryProgramme();
        Double totalDailyCalories = 2394.0;
        Integer mealsQuantity = 4;
        Calendar startDate = Calendar.getInstance();
        Integer currentDay = 3, lastDay = 14;

        totalDailyNutrients.clear();
        totalDailyNutrients.put("Protein", 108.0);
        totalDailyNutrients.put("Carbohydrate", 288.0);
        totalDailyNutrients.put("Fat", 90.0);

        for (Recipes qrecipe : allRecipes) {

            System.out.println(qrecipe.getRecipeName());
            System.out.println(qrecipe.getRecipeCalories());
        }

        DailyMenu dailyMenu = new DailyMenu();
        dailyMenu.setDailyMenuName("Day 3 of 14");

        doReturn(allRecipes).when(recipeService).getRecipes(any(RecipeGetRequest.class));

        doReturn(new ResponseMessage("Recipe is required")).when(mealService).addNewMeal(anyString(), any(Recipes.class), any(DailyMenu.class));

        doReturn(new ResponseMessage("Recipe is appropriate in regard to dietary preference")).when(spyDailyMenuService).verifyRecipe(any(Recipes.class), anyMap());

        ResponseMessage actual = spyDailyMenuService.addNewDailyMenu(dietaryProgramme, totalDailyCalories, mealsQuantity, totalDailyNutrients, currentDay, lastDay);

        assertEquals(expected, actual.getMessage());
    }

}