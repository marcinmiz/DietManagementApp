package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.DailyMenu;
import agh.edu.pl.diet.entities.DietaryProgramme;
import agh.edu.pl.diet.entities.Meals;
import agh.edu.pl.diet.payloads.request.DailyMenuRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.services.DailyMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menus")
public class DailyMenuController {

    @Autowired
    private DailyMenuService dailyMenuService;


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
//    @GetMapping("/programme/{id}")
//    public DietaryProgramme getDietaryProgramme(@PathVariable("id") Long dailyMenuId) {
//        return dailyMenuService.getDietaryProgramme((dailyMenuId));
//    }

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