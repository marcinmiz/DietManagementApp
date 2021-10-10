package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.DietType;
import agh.edu.pl.diet.services.DietTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dietTypes")
public class DietTypeController {

    @Autowired
    private DietTypeService dietTypeService;

    @GetMapping
    public ResponseEntity<List<DietType>> getAllDietTypes() {
        return ResponseEntity.status(HttpStatus.OK).body(dietTypeService.getAllDietTypes());
    }

}
