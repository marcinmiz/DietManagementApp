//package agh.edu.pl.diet.services;
//
//import agh.edu.pl.diet.entities.Product;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.atomic.AtomicLong;
//
//@Service
//public class ProductServices implements IProductService {
//
//    private final AtomicLong counter = new AtomicLong();
//
//    private final Set<Product> product = new HashSet<>(Set.of(new Product(),
//            new Product(), new Product(),
//            new Product()));
//
//
//    public boolean delete(Long id) {
//
//        var isRemoved = this.product.removeIf(product1 -> product1.getId().equals(id));
//
//        return isRemoved;
//    }
//
//    public Set<Product> all() {
//
//        return this.product;
//    }
//}