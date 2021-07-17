package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.Product;
import agh.edu.pl.diet.payloads.request.ProductRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;

import java.util.List;


public interface ProductService {

    List<Product> getAllProducts();

    Product getProduct(Long product_id);

    ResponseMessage addNewProduct(ProductRequest productRequest);

    ResponseMessage updateProduct(Long productId, ProductRequest productRequest);

//    Product save(Product product);
//
//    List<Product> findAll();
//
//    Product findOne(Long id);
//
    void removeOne(Long id);
}