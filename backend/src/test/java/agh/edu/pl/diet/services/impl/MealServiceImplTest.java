package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.DailyMenu;
import agh.edu.pl.diet.entities.Meals;
import agh.edu.pl.diet.entities.Recipes;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.MealRepo;
import agh.edu.pl.diet.services.MealService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class MealServiceImplTest {

    @Mock
    private MealRepo mealRepo;
    @InjectMocks
    private MealService mealService = new MealServiceImpl();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldAddNewMeal() {
        String expected = "Meal has been added";
        String mealName = "dinner";

        Recipes recipe = new Recipes();
        DailyMenu dailyMenu = new DailyMenu();

        doReturn(null).when(mealRepo).save(any(Meals.class));

        ResponseMessage actual = mealService.addNewMeal(mealName, recipe, dailyMenu);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldNotPassDueToIncorrectMealName() {
        String expected = "Wrong meal name";
        String mealName = "midnight";

        Recipes recipe = new Recipes();
        DailyMenu dailyMenu = new DailyMenu();

        ResponseMessage actual = mealService.addNewMeal(mealName, recipe, dailyMenu);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldNotPassDueToNullRecipe() {
        String expected = "Recipe is required";
        String mealName = "supper";

        Recipes recipe = null;
        DailyMenu dailyMenu = new DailyMenu();

        ResponseMessage actual = mealService.addNewMeal(mealName, recipe, dailyMenu);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldNotPassDueToNullDailyMenu() {
        String expected = "DailyMenu is required";
        String mealName = "dinner";

        Recipes recipe = new Recipes();
        DailyMenu dailyMenu = null;

        ResponseMessage actual = mealService.addNewMeal(mealName, recipe, dailyMenu);

        assertEquals(expected, actual.getMessage());
    }
}