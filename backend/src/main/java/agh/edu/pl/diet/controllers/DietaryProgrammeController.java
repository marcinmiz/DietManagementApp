package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.DietaryProgramme;
import agh.edu.pl.diet.payloads.request.DailyMenuRequest;
import agh.edu.pl.diet.payloads.request.DietaryProgrammeRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.services.DailyMenuService;
import agh.edu.pl.diet.services.DietaryProgrammeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import java.util.List;

@RestController
@RequestMapping("/api/programmes")
public class DietaryProgrammeController {

    @Autowired
    private DietaryProgrammeService dietaryProgrammeService;

    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addNewDietaryProgramme(@RequestBody DietaryProgrammeRequest dietaryProgrammeRequest) {
        ResponseMessage message = dietaryProgrammeService.addNewDietaryProgramme(dietaryProgrammeRequest);
        if (message.getMessage().equals("Dietary Programme has been added")) {
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @GetMapping
    public ResponseEntity<List<DietaryProgramme>> getUserDietaryProgrammes() {
        List<DietaryProgramme> userDietaryProgrammes = dietaryProgrammeService.getUserDietaryProgrammes();
        if (userDietaryProgrammes != null) {
            return ResponseEntity.status(HttpStatus.OK).body(userDietaryProgrammes);
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(userDietaryProgrammes);
        }
    }

    @PutMapping("/start/{id}")
    public ResponseEntity<ResponseMessage> startDietaryProgramme(@PathVariable("id") Long dietaryProgrammeId) {
        ResponseMessage responseMessage = dietaryProgrammeService.useDietaryProgramme(dietaryProgrammeId, "start");
        if (!responseMessage.getMessage().endsWith(" has been started")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PutMapping("/abandon/{id}")
    public ResponseEntity<ResponseMessage> abandonDietaryProgramme(@PathVariable("id") Long dietaryProgrammeId) {
        ResponseMessage responseMessage = dietaryProgrammeService.useDietaryProgramme(dietaryProgrammeId, "abandon");
        if (!responseMessage.getMessage().endsWith(" has been abandoned")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

//    @PutMapping("/update/{id}")
//    public ResponseMessage updateDailyMenu(@PathVariable("id") Long dailyMenuId, @RequestBody DailyMenuRequest dailyMenuRequest) {
//        return dailyMenuService.updateDailyMenu(dailyMenuId, dailyMenuRequest);
//    }
//
//    @DeleteMapping("/remove/{id}")
//    public ResponseMessage removeDailyMenu(@PathVariable("id") Long dailyMenuId) {
//        return dailyMenuService.removeDailyMenu(dailyMenuId);
//    }

}