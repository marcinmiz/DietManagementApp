package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.Product;
import agh.edu.pl.diet.payloads.request.ProductRequest;
import agh.edu.pl.diet.payloads.request.ProductSearchRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") Long productId) {
        return productService.getProduct(productId);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addNewProduct(@RequestBody ProductRequest productRequest) {
        ResponseMessage message = productService.addNewProduct(productRequest);
        if (message.getMessage().endsWith(" has been added successfully")) {
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseMessage updateProduct(@PathVariable("id") Long productId, @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(productId, productRequest);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseMessage removeProduct(@PathVariable("id") Long productId) {
        return productService.removeProduct(productId);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestBody ProductSearchRequest productSearchRequest) {
        List<Product> products = productService.searchProducts(productSearchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

}