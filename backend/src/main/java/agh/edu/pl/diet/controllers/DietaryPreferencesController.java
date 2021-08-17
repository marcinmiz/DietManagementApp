package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.DietaryPreferences;
import agh.edu.pl.diet.payloads.request.DietaryPreferencesRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.services.DietaryPreferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
public class DietaryPreferencesController {

   @Autowired
   private DietaryPreferencesService dietaryPreferencesService;

//    @GetMapping
//    public List<Product> getAllProducts() {
//        return dietaryPreferencesService.getAllProducts();
//    }

    @GetMapping("/{id}")
    public DietaryPreferences getDietaryPreferences(@PathVariable("id") Long dietaryPreferencesId) {
        return dietaryPreferencesService.getDietaryPreferences(dietaryPreferencesId);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addDietaryPreferences(@RequestBody DietaryPreferencesRequest dietaryPreferencesRequest) {
        ResponseMessage message = dietaryPreferencesService.addNewDietaryPreferences(dietaryPreferencesRequest);
        if (message.getMessage().endsWith(" has been added successfully")) {
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseMessage updateDietaryPreferences(@PathVariable("id") Long dietaryPreferencesId, @RequestBody DietaryPreferencesRequest dietaryPreferencesRequest) {
        return dietaryPreferencesService.updateDietaryPreferences(dietaryPreferencesId, dietaryPreferencesRequest);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseMessage removeDietaryPreferences(@PathVariable("id") Long dietaryPreferencesId) {
        return dietaryPreferencesService.removeDietaryPreferences(dietaryPreferencesId);
    }
}


