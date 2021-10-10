package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.DietaryPreferences;
import agh.edu.pl.diet.payloads.request.DietaryPreferencesRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.services.DietaryPreferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preferences")
public class DietaryPreferencesController {

    @Autowired
    private DietaryPreferencesService dietaryPreferencesService;

//    @GetMapping
//    public List<DietaryPreferences> getAllDietaryPreferences() {
//        return dietaryPreferencesService.getAllDietaryPreferences();
//    }

    @GetMapping
    public ResponseEntity<List<DietaryPreferences>> getUserDietaryPreferences() {
        List<DietaryPreferences> list = dietaryPreferencesService.getUserDietaryPreferences();
        if (list == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(list);
        }
    }

    @GetMapping("/{id}")
    public DietaryPreferences getDietaryPreferences(@PathVariable("id") Long dietaryPreferencesId) {
        return dietaryPreferencesService.getDietaryPreferences(dietaryPreferencesId);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addNewDietaryPreferences(@RequestBody DietaryPreferencesRequest dietaryPreferencesRequest) {
        ResponseMessage message = dietaryPreferencesService.addNewDietaryPreferences(dietaryPreferencesRequest);
        if (message.getMessage().equals("Dietary Preference has been added")) {
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> updateDietaryPreferences(@PathVariable("id") Long dietaryPreferenceId, @RequestBody DietaryPreferencesRequest dietaryPreferencesRequest) {

        ResponseMessage message = dietaryPreferencesService.updateDietaryPreferences(dietaryPreferenceId, dietaryPreferencesRequest);
        if (message.getMessage().equals("Dietary Preferences " + dietaryPreferenceId + " has been updated")) {
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ResponseMessage> removeDietaryPreferences(@PathVariable("id") Long dietaryPreferenceId) {
        ResponseMessage message = dietaryPreferencesService.removeDietaryPreferences(dietaryPreferenceId);
        if (message.getMessage().equals("Dietary Preference " + dietaryPreferenceId + " has been removed")) {
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }
}


