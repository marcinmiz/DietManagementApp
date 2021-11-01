package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.Product;
import agh.edu.pl.diet.services.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shopping")
public class ShoppingListController {

    @Autowired
    ShoppingListService shoppingListService;

    @GetMapping
    public ResponseEntity<List<List<String>>> getShoppingList() {
        List<List<String>> shoppingList = shoppingListService.getShoppingList();
        if (shoppingList == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(shoppingList);
    }
}
