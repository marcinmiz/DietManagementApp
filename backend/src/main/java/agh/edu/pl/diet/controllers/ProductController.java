package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.Product;
import agh.edu.pl.diet.payloads.request.ItemAssessRequest;
import agh.edu.pl.diet.payloads.request.ProductGetRequest;
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
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> productList = productService.getAllProducts();
        if (productList == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @PostMapping
    public ResponseEntity<List<Product>> getProducts(@RequestBody ProductGetRequest productGetRequest) {
        List<Product> productList = productService.getProducts(productGetRequest);
        if (productList == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") Long productId) {
        return productService.getProduct(productId);
    }

    @GetMapping("/checkProductApprovalStatus/{id}")
    public ResponseEntity<ResponseMessage> checkProductApprovalStatus(@PathVariable("id") Long productId) {
        ResponseMessage result = productService.checkProductApprovalStatus(productId);

        if (result.getMessage().equals("Product with id " + productId + " has not been found")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
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

    @PostMapping("/assess")
    public ResponseEntity<ResponseMessage> assessProduct(@RequestBody ItemAssessRequest itemAssessRequest) {
        ResponseMessage responseMessage = productService.assessProduct(itemAssessRequest);
        if (!responseMessage.getMessage().endsWith("has been accepted") && !responseMessage.getMessage().endsWith("has been rejected")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

}