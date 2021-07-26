package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.Product;
import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.entities.security.UserRole;
import agh.edu.pl.diet.payloads.request.ProductRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;

import java.util.List;
import java.util.Set;


public interface UserService {
    User getCreateUser(User user, Set<UserRole> userRoles) throws Exception;

    User save(User user);

    List<Product> getAllProducts();

    Product getProduct(Long product_id);

    ResponseMessage addNewProduct(ProductRequest productRequest);

    ResponseMessage updateProduct(Long productId, ProductRequest productRequest);

    ResponseMessage removeProduct(Long productId);
}