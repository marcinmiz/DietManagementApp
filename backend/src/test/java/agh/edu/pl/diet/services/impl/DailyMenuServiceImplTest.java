package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.*;
import agh.edu.pl.diet.payloads.request.RecipeGetRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.services.DailyMenuService;
import agh.edu.pl.diet.services.RecipeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class DailyMenuServiceImplTest {

    private List<Category> categories = List.of(new Category("Fruit"), new Category("Vegetables"), new Category("Cereal"), new Category("Dairy"), new Category("Meat"), new Category("Fish"), new Category("Fats"), new Category("Sweets"), new Category("Nuts"));
    @Mock
    private RecipeService recipeService;
    @InjectMocks
    private DailyMenuService dailyMenuService = new DailyMenuServiceImpl();
    private Map<String, List<Double>> nutrientsScopes;
    private Map<String, Double> totalDailyNutrients;
    private List<Nutrient> nutrients;
    private List<List<ProductNutrient>> productNutrients;
    private List<Product> products;
    private List<RecipeProduct> recipeProducts = new ArrayList<>();
    private List<RecipeCustomerSatisfaction> satisfactions;
    private List<Recipes> collection;

    @BeforeEach
    void setUp() {
        nutrientsScopes = new LinkedHashMap<>();
        totalDailyNutrients = new LinkedHashMap<>();

        nutrientsScopes.put("breakfast", List.of(0.25, 0.3));
        nutrientsScopes.put("lunch", List.of(0.05, 0.1));
        nutrientsScopes.put("dinner", List.of(0.35, 0.4));
        nutrientsScopes.put("supper", List.of(0.25, 0.3));

        totalDailyNutrients.put("Protein", 120.0);
        totalDailyNutrients.put("Carbohydrate", 80.0);
        totalDailyNutrients.put("Fat", 40.0);

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

        for (Product currentProduct: products) {
            RecipeProduct recipeProduct = new RecipeProduct();
            recipeProducts.add(recipeProduct);
        }

        satisfactions = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            RecipeCustomerSatisfaction satisfaction = new RecipeCustomerSatisfaction();
            satisfactions.add(satisfaction);
        }

//        Mockito.when(recipeService.getRecipes(any(RecipeGetRequest.class))).thenReturn(collection);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldRecipeBeAppropriate() {
        String expected = "Recipe is appropriate in regard to dietary preference";
        String mealName = "breakfast";

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

        ResponseMessage actual = dailyMenuService.verifyRecipe(recipe, nutrientsScopes.get(mealName), totalDailyNutrients);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldRecipeBeInappropriateDueToAmountNutrient() {
        String expected = "Recipe has inappropriate amount of Fat";
        String mealName = "breakfast";

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

        ResponseMessage actual = dailyMenuService.verifyRecipe(recipe, nutrientsScopes.get(mealName), totalDailyNutrients);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldRecipeBeInappropriateDueToProductCategories() {
        String expected = "Recipe is inappropriate in regard to dietary preference";
        String mealName = "breakfast";

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

        ResponseMessage actual = dailyMenuService.verifyRecipe(recipe, nutrientsScopes.get(mealName), totalDailyNutrients);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldRecipeBeInappropriateDueToLowRatings() {
        String expected = "Recipe is inappropriate in regard to dietary preference";
        String mealName = "breakfast";

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

        ResponseMessage actual = dailyMenuService.verifyRecipe(recipe, nutrientsScopes.get(mealName), totalDailyNutrients);

        assertEquals(expected, actual.getMessage());
    }

}