package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepo extends CrudRepository<Product, Long> {

}
