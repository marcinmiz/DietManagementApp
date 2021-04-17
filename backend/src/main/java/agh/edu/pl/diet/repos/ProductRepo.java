package agh.edu.pl.diet.repos;

import agh.edu.pl.diet.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Repository
public interface ProductRepo extends JpaRepository<Product, Long>
{
    List<Product> findByUserId(Long userId);
}
