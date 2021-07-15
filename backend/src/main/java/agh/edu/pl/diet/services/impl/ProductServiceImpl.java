package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.Nutrient;
import agh.edu.pl.diet.entities.Product;
import agh.edu.pl.diet.entities.ProductNutrient;
import agh.edu.pl.diet.payloads.request.ProductRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.CategoryRepo;
import agh.edu.pl.diet.repos.NutrientRepo;
import agh.edu.pl.diet.repos.ProductRepo;
import agh.edu.pl.diet.repos.UserRepo;
import agh.edu.pl.diet.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private NutrientRepo nutrientRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private UserRepo userRepo;

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
    public ResponseMessage addNewProduct(ProductRequest productRequest) {
        Product product = new Product();
        String productName = productRequest.getProductName();
        if (productName == null) {
            return new ResponseMessage("Product name has to be given");
        } else if (productName.length() < 2 || productName.length() > 40) {
            return new ResponseMessage("Product name has to have min 2 and max 40 characters");
        } else if (!(productName.matches("^[a-zA-Z ]+$"))) {
            return new ResponseMessage("Product name has to contain only letters and spaces");
        } else if (productRepo.findByProductName(productName) != null) {
            return new ResponseMessage("Product with this name exists yet");
        }

        product.setProductName(productName);

        Integer calories = productRequest.getCalories();
        if (calories == null) {
            return new ResponseMessage("Product calories has to be given");
        } else if (calories.toString().length() < 1 || calories.toString().length() > 10) {
            return new ResponseMessage("Product calories has to have min 1 and max 10 characters");
        } else if (!(calories.toString().matches("^0$") || calories.toString().matches("^(-)?[1-9]\\d*$"))) {
            return new ResponseMessage("Product calories has to contain only digits");
        } else if (calories < 0) {
            return new ResponseMessage("Product calories has to be greater or equal 0");
        }
        product.setCalories(calories);

        String categoryName = productRequest.getCategory();

        if (categoryName == null) {
            return new ResponseMessage("Product category has to be given");
        } else if (categoryName.equals("")) {
            return new ResponseMessage("Product category has to be chosen");
        } else if (categoryRepo.findByCategoryName(categoryName) == null) {
            return new ResponseMessage("Product category does not exist");
        }
        product.setCategory(categoryRepo.findByCategoryName(categoryName));

        List<String> nutrients = productRequest.getNutrients();

        if (nutrients == null) {
            return new ResponseMessage("Product nutrients has to be given");
        } else if (nutrients.isEmpty()) {
            return new ResponseMessage("At least 1 product nutrient is required");
        }

        for (String nutrientStatement: nutrients) {
            System.out.println(nutrientStatement);
            if (nutrientStatement.equals("")) {
                return new ResponseMessage("Product nutrient has to be defined");
            } else if (!(nutrientStatement.matches("^[a-zA-Z]+;0$") || nutrientStatement.matches("^[a-zA-Z]+;(-)?[1-9]\\d*$"))) {
                return new ResponseMessage("Product nutrient has to match format \"productName;productAmount\"");
            }
            String[] parts = nutrientStatement.split(";");
            String nutrientName = parts[0];
            Double nutrientAmount = Double.valueOf(parts[1]);

            if (nutrientRepo.findByNutrientName(nutrientName) == null) {
                return new ResponseMessage("Product nutrient name has to be proper");
            }
            if (nutrientAmount.toString().length() < 1 || nutrientAmount.toString().length() > 20) {
                return new ResponseMessage("Product nutrient amount has to have min 1 and max 20 characters");
            } else if (nutrientAmount < 0) {
                return new ResponseMessage("Product nutrient amount has to be greater or equal 0");
            }
            //change to logged in user id
            product.setOwner(userRepo.findById(1L).get());
            product.setCreationDate(new Date());
            Nutrient nutrient = nutrientRepo.findByNutrientName(nutrientName);
            ProductNutrient productNutrient = new ProductNutrient();
            productNutrient.setNutrient(nutrient);
            productNutrient.setNutrientAmount(nutrientAmount);
            productNutrient.setProduct(product);
            product.addNutrient(productNutrient);
        }
        productRepo.save(product);
        return new ResponseMessage("Product has been added successfully");
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