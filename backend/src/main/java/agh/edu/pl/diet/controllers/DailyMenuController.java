package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.DailyMenu;
import agh.edu.pl.diet.entities.DietaryProgramme;
import agh.edu.pl.diet.entities.Meals;
import agh.edu.pl.diet.entities.Recipes;
import agh.edu.pl.diet.payloads.request.DailyMenuRequest;
import agh.edu.pl.diet.payloads.request.RecipeGetRequest;
import agh.edu.pl.diet.payloads.response.DailyMenuResponse;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.MealRepo;
import agh.edu.pl.diet.services.DailyMenuService;
import agh.edu.pl.diet.services.MealService;
import agh.edu.pl.diet.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class DailyMenuController {

    @Autowired
    private DailyMenuService dailyMenuService;

    @Autowired
    private MealService mealService;

    @Autowired
    private RecipeService recipeService;


//    @GetMapping("/{id}")
//    public DailyMenu getDailyMenu(@PathVariable("id") Long dailyMenuId) {
//        return dailyMenuService.getDailyMenu(dailyMenuId);
//    }
//
//    @GetMapping("/meal/{id}")
//    public Meals getMeal(@PathVariable("id") Long dailyMenuId) {
//        return dailyMenuService.getMeals(dailyMenuId);
//    }
//
    @GetMapping("/{programmeId}")
    public ResponseEntity<List<DailyMenuResponse>> getDietaryProgrammeDailyMenus(@PathVariable("programmeId") Long programmeId) {
        List<DailyMenuResponse> menuResponses = dailyMenuService.getDietaryProgrammeDailyMenus(programmeId);
        if (menuResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(menuResponses);
        }
        return ResponseEntity.status(HttpStatus.OK).body(menuResponses);
    }

    @GetMapping("/markMealAsConsumed/{mealId}/{mark}")
    public ResponseEntity<ResponseMessage> markMealAsConsumed(@PathVariable("mealId") Long mealId, @PathVariable("mark") String mark) {
        ResponseMessage message = mealService.markMealAsConsumed(mealId, mark);
        if (message.getMessage().endsWith("has been marked as consumed") || message.getMessage().endsWith("has been unmarked as consumed")) {
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
    }

    @GetMapping("/getCaloriesConsumed")
    public ResponseEntity<List<Double>> getCaloriesConsumed() {
        List<Double> consumed = mealService.getCaloriesConsumed();
        if (consumed != null) {
            return ResponseEntity.status(HttpStatus.OK).body(consumed);
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(consumed);
    }

//    @PostMapping("/add")
//    public ResponseEntity<ResponseMessage> addNewDailyMenu(@RequestBody DailyMenuRequest dailyMenuRequest) {
//        ResponseMessage message = dailyMenuService.addNewDailyMenu(dailyMenuRequest);
//        if (message.getMessage().endsWith(" has been added successfully")) {
//            return ResponseEntity.status(HttpStatus.OK).body(message);
//        } else {
//            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
//        }
//    }

    @PutMapping("/update/{id}")
    public ResponseMessage updateDailyMenu(@PathVariable("id") Long dailyMenuId, @RequestBody DailyMenuRequest dailyMenuRequest) {
        return dailyMenuService.updateDailyMenu(dailyMenuId, dailyMenuRequest);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseMessage removeDailyMenu(@PathVariable("id") Long dailyMenuId) {
        return dailyMenuService.removeDailyMenu(dailyMenuId);
    }

}