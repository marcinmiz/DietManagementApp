package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends CrudRepository<Product, Long> {
    Product findByProductName(String productName);

}