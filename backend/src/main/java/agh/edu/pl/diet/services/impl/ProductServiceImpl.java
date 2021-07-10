package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.Product;
import agh.edu.pl.diet.payloads.ProductRequest;
import agh.edu.pl.diet.repos.ProductRepo;
import agh.edu.pl.diet.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Override
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        productRepo.findAll().forEach(list::add);
        return list;
    }

    @Override
    public Product getProduct(Long product_id) {
        return productRepo.findById(product_id).get();
    }

    @Override
    public Product addNewProduct(ProductRequest productRequest) {
        System.out.println(productRequest);
        System.out.println(productRequest.getProductName());
        Product product = new Product();
        product.setProductName(productRequest.getProductName());
        return productRepo.save(product);
    }

    //    public Product save(Product product) {
//        return productRepo.save(product);
//    }
//
//    public List<Product> findAll() {
//        return (List<Product>) productRepo.findAll();
//    }
//
//    @Override
//    public Product findOne(Long id) {
//        return null;
//    }
//
    @Override
    public void removeOne(Long id) {

    }

//	public Product findOne(Long id) {
//		return productRepo.findOne(id);
//	}

//	public void removeOne(Long id) {
//		productRepo.delete(id);
//	}
}