package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.*;
import agh.edu.pl.diet.payloads.request.DietaryProgrammeRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.DailyMenuRepo;
import agh.edu.pl.diet.repos.DietaryProgrammeRepo;
import agh.edu.pl.diet.repos.MealRepo;
import agh.edu.pl.diet.repos.UserRepo;
import agh.edu.pl.diet.services.DailyMenuService;
import agh.edu.pl.diet.services.DietaryPreferencesService;
import agh.edu.pl.diet.services.DietaryProgrammeService;
import agh.edu.pl.diet.services.UserService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class DietaryProgrammeServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private DietaryPreferencesService dietaryPreferencesService;
    @Mock
    private DailyMenuService dailyMenuService;
    @Mock
    private DietaryProgrammeRepo dietaryProgrammeRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private DailyMenuRepo dailyMenuRepo;
    @Mock
    private MealRepo mealRepo;
    @InjectMocks
    private DietaryProgrammeService dietaryProgrammeService = new DietaryProgrammeServiceImpl();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldAddNewDietaryProgramme() {
        String expected = "Dietary Programme has been added";

        Long preferenceId = 1L;
        Integer dietaryProgrammeDays = 3;
        String dietaryProgrammeName = "wonderful diet";

        String[] nutrientNames = new String[]{"Protein", "Carbohydrate", "Fat"};
        double[] nutrientAmounts = new double[]{108.0, 288.0, 90.0};

        DietaryProgrammeRequest request = new DietaryProgrammeRequest();

        request.setDietaryProgrammeName(dietaryProgrammeName);
        request.setDietaryProgrammeDays(dietaryProgrammeDays);
        request.setPreferenceId(preferenceId);

        User loggedUser = new User();
        loggedUser.setName("John");
        loggedUser.setUsername("johnny");

        Set<DietaryPreferencesNutrient> nutrients = new LinkedHashSet<>();

        for (int i = 0; i < 3; i++) {
            DietaryPreferencesNutrient preferencesNutrient = new DietaryPreferencesNutrient();
            Nutrient nutrient = new Nutrient();
            nutrient.setNutrientName(nutrientNames[i]);
            preferencesNutrient.setNutrient(nutrient);
            preferencesNutrient.setNutrientAmount(nutrientAmounts[i]);
            nutrients.add(preferencesNutrient);
        }

        DietaryPreferences dietaryPreference = new DietaryPreferences();
        dietaryPreference.setTotalDailyCalories(2394.0);
        dietaryPreference.setMealsQuantity(4);
        dietaryPreference.setNutrients(nutrients);

        doReturn(loggedUser).when(userService).getLoggedUser();
        doReturn(loggedUser).when(userService).findByUsername(anyString());

        doReturn(dietaryPreference).when(dietaryPreferencesService).getDietaryPreferences(anyLong());

        doReturn(new ResponseMessage("Daily Menu has been added")).when(dailyMenuService).addNewDailyMenu(any(DietaryProgramme.class), anyDouble(), anyInt(), anyMap(), anyInt(), anyInt());

        doReturn(null).when(dietaryProgrammeRepo).save(any(DietaryProgramme.class));

        ResponseMessage actual = dietaryProgrammeService.addNewDietaryProgramme(request);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldFailDueToNotLoggedUser() {
        String expected = "Dietary programme owner has not been found";

        Long preferenceId = 1L;
        Integer dietaryProgrammeDays = 3;
        String dietaryProgrammeName = "wonderful diet";

        DietaryProgrammeRequest request = new DietaryProgrammeRequest();

        request.setDietaryProgrammeName(dietaryProgrammeName);
        request.setDietaryProgrammeDays(dietaryProgrammeDays);
        request.setPreferenceId(preferenceId);

        User loggedUser = new User();
        loggedUser.setName("John");
        loggedUser.setUsername("johnny");

        doReturn(loggedUser).when(userService).getLoggedUser();
        doReturn(null).when(userService).findByUsername(anyString());

        ResponseMessage actual = dietaryProgrammeService.addNewDietaryProgramme(request);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldFailDueToWrongDietaryProgrammeDays() {
        String expected = "Dietary programme days has to be at least equal to 1";

        Long preferenceId = 1L;
        Integer dietaryProgrammeDays = 0;
        String dietaryProgrammeName = "wonderful diet";

        DietaryProgrammeRequest request = new DietaryProgrammeRequest();

        request.setDietaryProgrammeName(dietaryProgrammeName);
        request.setDietaryProgrammeDays(dietaryProgrammeDays);
        request.setPreferenceId(preferenceId);

        User loggedUser = new User();
        loggedUser.setName("John");
        loggedUser.setUsername("johnny");

        doReturn(loggedUser).when(userService).getLoggedUser();
        doReturn(loggedUser).when(userService).findByUsername(anyString());

        ResponseMessage actual = dietaryProgrammeService.addNewDietaryProgramme(request);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldFailDueToWrongPreferenceId() {

        Long preferenceId = 1L;
        Integer dietaryProgrammeDays = 3;
        String dietaryProgrammeName = "wonderful diet";

        String expected = "Dietary preference with id " + preferenceId + " has not been found";

        DietaryProgrammeRequest request = new DietaryProgrammeRequest();

        request.setDietaryProgrammeName(dietaryProgrammeName);
        request.setDietaryProgrammeDays(dietaryProgrammeDays);
        request.setPreferenceId(preferenceId);

        User loggedUser = new User();
        loggedUser.setName("John");
        loggedUser.setUsername("johnny");

        doReturn(loggedUser).when(userService).getLoggedUser();
        doReturn(loggedUser).when(userService).findByUsername(anyString());

        doReturn(null).when(dietaryPreferencesService).getDietaryPreferences(anyLong());

        ResponseMessage actual = dietaryProgrammeService.addNewDietaryProgramme(request);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldFailDueToNotCreatedDailyMenu() {

        String expected = "Daily Menu has not been created";

        Long preferenceId = 1L;
        Integer dietaryProgrammeDays = 3;
        String dietaryProgrammeName = "wonderful diet";

        String[] nutrientNames = new String[]{"Protein", "Carbohydrate", "Fat"};
        double[] nutrientAmounts = new double[]{108.0, 288.0, 90.0};

        DietaryProgrammeRequest request = new DietaryProgrammeRequest();

        request.setDietaryProgrammeName(dietaryProgrammeName);
        request.setDietaryProgrammeDays(dietaryProgrammeDays);
        request.setPreferenceId(preferenceId);

        User loggedUser = new User();
        loggedUser.setName("John");
        loggedUser.setUsername("johnny");

        Set<DietaryPreferencesNutrient> nutrients = new LinkedHashSet<>();

        for (int i = 0; i < 3; i++) {
            DietaryPreferencesNutrient preferencesNutrient = new DietaryPreferencesNutrient();
            Nutrient nutrient = new Nutrient();
            nutrient.setNutrientName(nutrientNames[i]);
            preferencesNutrient.setNutrient(nutrient);
            preferencesNutrient.setNutrientAmount(nutrientAmounts[i]);
            nutrients.add(preferencesNutrient);
        }

        DietaryPreferences dietaryPreference = new DietaryPreferences();
        dietaryPreference.setTotalDailyCalories(2394.0);
        dietaryPreference.setMealsQuantity(4);
        dietaryPreference.setNutrients(nutrients);

        doReturn(loggedUser).when(userService).getLoggedUser();
        doReturn(loggedUser).when(userService).findByUsername(anyString());

        doReturn(dietaryPreference).when(dietaryPreferencesService).getDietaryPreferences(anyLong());

        doReturn(new ResponseMessage("No recipe is appropriate for lunch")).when(dailyMenuService).addNewDailyMenu(any(DietaryProgramme.class), anyDouble(), anyInt(), anyMap(), anyInt(), anyInt());

        ResponseMessage actual = dietaryProgrammeService.addNewDietaryProgramme(request);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldStartDietaryProgramme() {
        Long programmeId = 980L;
        String programmeName = "Amazing experience";
        String type = "start";
        User user = new User();
        Integer programmeDays = 3;

        String expected = "Dietary programme " + programmeName + " has been started";

        doReturn(null).when(userRepo).save(any(User.class));
        doReturn(null).when(dailyMenuRepo).save(any(DailyMenu.class));
        doReturn(null).when(mealRepo).save(any(Meals.class));

        DietaryProgramme programme = new DietaryProgramme();
        programme.setDietaryProgrammeName(programmeName);
        programme.setDietaryProgrammeDays(programmeDays);
        programme.setOwner(user);
        Optional<DietaryProgramme> programmeOptional = Optional.of(programme);

        doReturn(programmeOptional).when(dietaryProgrammeRepo).findById(anyLong());

        List<DailyMenu> menus = List.of(new DailyMenu(), new DailyMenu(), new DailyMenu());
        doReturn(menus).when(dailyMenuRepo).findByDietaryProgramme(any(DietaryProgramme.class));

        Meals meal1 = new Meals();
        meal1.setMealsName("breakfast");
        Meals meal2 = new Meals();
        meal2.setMealsName("lunch");
        Meals meal3 = new Meals();
        meal3.setMealsName("dinner");
        Meals meal4 = new Meals();
        meal4.setMealsName("supper");

        List<Meals> meals = List.of(meal1, meal2, meal3, meal4);
        doReturn(meals).when(mealRepo).findByDailyMenu(any(DailyMenu.class));

        ResponseMessage actual = dietaryProgrammeService.useDietaryProgramme(programmeId, type);

        assertAll(
                () -> assertEquals(expected, actual.getMessage()),
                () -> assertNotNull(user.getCurrentDietaryProgramme()),
                () -> assertNotNull(user.getCurrentDietaryProgrammeDay()),
                () -> assertNotNull(user.getDietaryProgrammeStartDate())
        );
    }

    @Test
    void shouldAbandonDietaryProgramme() {
        Long programmeId = 980L;
        String programmeName = "Amazing experience";
        String type = "abandon";

        String expected = "Dietary programme " + programmeName + " has been abandoned";

        doReturn(null).when(userRepo).save(any(User.class));
        doReturn(null).when(dailyMenuRepo).save(any(DailyMenu.class));
        doReturn(null).when(mealRepo).save(any(Meals.class));

        DietaryProgramme programme = new DietaryProgramme();
        programme.setDietaryProgrammeName(programmeName);
        programme.setDietaryProgrammeDays(3);

        User user = new User();
        user.setDietaryProgrammeStartDate(Calendar.getInstance().toInstant().toString());
        user.setCurrentDietaryProgramme(programme);
        user.setCurrentDietaryProgrammeDay(2);

        programme.setOwner(user);
        Optional<DietaryProgramme> programmeOptional = Optional.of(programme);

        doReturn(programmeOptional).when(dietaryProgrammeRepo).findById(anyLong());

        List<DailyMenu> menus = List.of(new DailyMenu(), new DailyMenu(), new DailyMenu());
        doReturn(menus).when(dailyMenuRepo).findByDietaryProgramme(any(DietaryProgramme.class));

        Meals meal1 = new Meals();
        meal1.setMealsName("breakfast");
        Meals meal2 = new Meals();
        meal2.setMealsName("lunch");
        Meals meal3 = new Meals();
        meal3.setMealsName("dinner");
        Meals meal4 = new Meals();
        meal4.setMealsName("supper");

        List<Meals> meals = List.of(meal1, meal2, meal3, meal4);
        doReturn(meals).when(mealRepo).findByDailyMenu(any(DailyMenu.class));

        ResponseMessage actual = dietaryProgrammeService.useDietaryProgramme(programmeId, type);

        assertAll(
                () -> assertEquals(expected, actual.getMessage()),
                () -> assertNull(user.getCurrentDietaryProgramme()),
                () -> assertNull(user.getCurrentDietaryProgrammeDay()),
                () -> assertNull(user.getDietaryProgrammeStartDate())
        );
    }

    @Test
    void shouldFailDueToWrongType() {
        Long programmeId = 980L;
        String type = "stop";

        String expected = "Type should be equal to \"start\" or \"abandon\"";

        ResponseMessage actual = dietaryProgrammeService.useDietaryProgramme(programmeId, type);

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void shouldFailDueToNotFoundDietaryProgramme() {
        Long programmeId = 950L;
        String type = "start";

        String expected = "Dietary programme with id " + programmeId + " has not been found";
        Optional<DietaryProgramme> dietaryProgrammeOptional = Optional.empty();

        doReturn(dietaryProgrammeOptional).when(dietaryProgrammeRepo).findById(anyLong());

        ResponseMessage actual = dietaryProgrammeService.useDietaryProgramme(programmeId, type);

        assertEquals(expected, actual.getMessage());
    }
}