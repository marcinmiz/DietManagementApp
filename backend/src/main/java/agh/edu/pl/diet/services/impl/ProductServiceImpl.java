package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.Category;
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

import java.util.*;

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

    private ResponseMessage verify(String mode, String type, Object item) {
        System.out.println("type " + type);
        switch (type) {
            case "name":
                String name = String.valueOf(item);
                if (name == null) {
                    return new ResponseMessage("Product name has to be given");
                } else if (name.length() < 2 || name.length() > 40) {
                    return new ResponseMessage("Product name has to have min 2 and max 40 characters");
                } else if (!(name.matches("^[a-zA-Z ]+$"))) {
                    return new ResponseMessage("Product name has to contain only letters and spaces");
                } else if (!mode.equals("update") && productRepo.findByProductName(name) != null) {
                    return new ResponseMessage("Product with this name exists yet");
                } else {
                    return new ResponseMessage("Product name is valid");
                }
            case "calories":
                if (item == null) {
                    return new ResponseMessage("Product calories has to be given");
                }

                Integer calories = Integer.parseInt(item.toString());

                if (calories.toString().length() < 1 || calories.toString().length() > 10) {
                    return new ResponseMessage("Product calories has to have min 1 and max 10 characters");
                } else if (!(calories.toString().matches("^0$") || calories.toString().matches("^(-)?[1-9]\\d*$"))) {
                    return new ResponseMessage("Product calories has to contain only digits");
                } else if (calories < 0) {
                    return new ResponseMessage("Product calories has to be greater or equal 0");
                } else {
                    return new ResponseMessage("Product calories are valid");
                }
            case "category":
                String categoryName = String.valueOf(item);
                if (categoryName == null) {
                    return new ResponseMessage("Product category has to be given");
                } else if (categoryName.equals("")) {
                    return new ResponseMessage("Product category has to be chosen");
                } else if (categoryRepo.findByCategoryName(categoryName) == null) {
                    return new ResponseMessage("Product category does not exist");
                } else {
                    return new ResponseMessage("Product category is valid");
                }
            case "list":
                List<String> nutrients = (List<String>) item;
                if (nutrients == null) {
                    return new ResponseMessage("Product nutrients has to be given");
                } else if (nutrients.isEmpty()) {
                    return new ResponseMessage("At least 1 product nutrient is required");
                } else {
                    return new ResponseMessage("Product nutrients are valid");
                }
            case "nutrientStatement":
                String nutrientStatement = String.valueOf(item);
                if (nutrientStatement.equals("")) {
                    return new ResponseMessage("Product nutrient has to be defined");
                } else if (!(nutrientStatement.matches("^[a-zA-Z]+;0$") || nutrientStatement.matches("^[a-zA-Z]+;(-)?[1-9]\\d*$"))) {
                    return new ResponseMessage("Product nutrient has to match format \"productName;productAmount\"");
                } else {
                    return new ResponseMessage("Product nutrient statement is valid");
                }
            case "nutrientName":
                String nutrientName = String.valueOf(item);

                if (nutrientRepo.findByNutrientName(nutrientName) == null) {
                    return new ResponseMessage("Product nutrient name has to be proper");
                } else {
                    return new ResponseMessage("Product nutrient name is valid");
                }
            case "nutrientAmount":
                Double nutrientAmount = Double.valueOf(item.toString());

                if (nutrientAmount.toString().length() < 1 || nutrientAmount.toString().length() > 20) {
                    return new ResponseMessage("Product nutrient amount has to have min 1 and max 20 characters");
                } else if (nutrientAmount < 0) {
                    return new ResponseMessage("Product nutrient amount has to be greater or equal 0");
                } else {
                    return new ResponseMessage("Product nutrient amount is valid");
                }

        }

        return new ResponseMessage("Invalid type");
    }

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

        ResponseMessage responseMessage = verify("add", "name", productName);
        if (responseMessage.getMessage().equals("Product name is valid")){
            product.setProductName(productName);
        } else {
            return responseMessage;
        }

        Integer calories = productRequest.getCalories();

        ResponseMessage responseMessage2 = verify("add","calories", calories);

        if (responseMessage2.getMessage().equals("Product calories are valid")) {
            product.setCalories(calories);
        } else {
            return responseMessage2;
        }

        String categoryName = productRequest.getCategory();

        ResponseMessage responseMessage3 = verify("add", "category", categoryName);

        if (responseMessage3.getMessage().equals("Product category is valid")) {
            product.setCategory(categoryRepo.findByCategoryName(categoryName));
        } else {
            return responseMessage3;
        }

        List<String> nutrients = productRequest.getNutrients();

        ResponseMessage responseMessage4 = verify("add", "list", nutrients);

        if (!(responseMessage4.getMessage().equals("Product nutrients are valid"))) {
            return responseMessage4;
        }

        for (String nutrientStatement: nutrients) {

            ResponseMessage responseMessage5 = verify("add","nutrientStatement", nutrientStatement);

            if (!(responseMessage5.getMessage().equals("Product nutrient statement is valid"))) {
                return responseMessage5;
            }
            String[] parts = nutrientStatement.split(";");
            String nutrientName = parts[0];
            Double nutrientAmount = Double.valueOf(parts[1]);

            ResponseMessage responseMessage6 = verify("add","nutrientName", nutrientName);

            if (!(responseMessage6.getMessage().equals("Product nutrient name is valid"))) {
                return responseMessage6;
            }

            ResponseMessage responseMessage7 = verify("add","nutrientAmount", nutrientAmount);

            if (!(responseMessage7.getMessage().equals("Product nutrient amount is valid"))) {
                return responseMessage7;
            }

            Nutrient nutrient = nutrientRepo.findByNutrientName(nutrientName);
            ProductNutrient productNutrient = new ProductNutrient();
            productNutrient.setNutrient(nutrient);
            productNutrient.setNutrientAmount(nutrientAmount);
            productNutrient.setProduct(product);
            product.addNutrient(productNutrient);
            //change to logged in user id
            product.setOwner(userRepo.findById(1L).get());
            product.setCreationDate(new Date());
        }
        productRepo.save(product);
        return new ResponseMessage("Product " + productName + " has been added successfully");
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