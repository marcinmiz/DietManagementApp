package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.Product;

import java.util.List;


public interface ProductService {

    Product save(Product product);

    List<Product> findAll();

    Product findOne(Long id);

    void removeOne(Long id);
}