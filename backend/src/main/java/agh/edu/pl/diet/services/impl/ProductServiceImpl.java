package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.controllers.ImageController;
import agh.edu.pl.diet.entities.*;
import agh.edu.pl.diet.payloads.request.ItemAssessRequest;
import agh.edu.pl.diet.payloads.request.ProductGetRequest;
import agh.edu.pl.diet.payloads.request.ProductRequest;
import agh.edu.pl.diet.payloads.request.ProductSearchRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.CategoryRepo;
import agh.edu.pl.diet.repos.NutrientRepo;
import agh.edu.pl.diet.repos.ProductRepo;
import agh.edu.pl.diet.repos.UserRepo;
import agh.edu.pl.diet.services.ProductService;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final Path root = Paths.get("images/products");

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private NutrientRepo nutrientRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;

    private ResponseMessage verify(String mode, String type, Object item) {
        switch (type) {
            case "name":

                if (item == null) {
                    return new ResponseMessage("Product name is required");
                }

                String name = String.valueOf(item);

                if (name.length() < 2 || name.length() > 40) {
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
                    return new ResponseMessage("Product calories are required");
                }

                Integer calories = Integer.parseInt(item.toString());

                if (calories.toString().length() < 1 || calories.toString().length() > 10) {
                    return new ResponseMessage("Product calories has to have min 1 and max 10 characters");
                } else if (!(calories.toString().matches("^0$") || calories.toString().matches("^(-)?[1-9]\\d*$"))) {
                    return new ResponseMessage("Product calories has to contain only digits");
                } else if (calories <= 0) {
                    return new ResponseMessage("Product calories has to be greater than 0");
                } else {
                    return new ResponseMessage("Product calories are valid");
                }
            case "category":
                String categoryName = String.valueOf(item);
                if (categoryName == null) {
                    return new ResponseMessage("Product category is required");
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
                    return new ResponseMessage("Product nutrients are required");
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
                } else if (nutrientAmount <= 0) {
                    return new ResponseMessage("Product nutrient amount has to be greater than 0");
                } else {
                    return new ResponseMessage("Product nutrient amount is valid");
                }

        }

        return new ResponseMessage("Invalid type");
    }

    private String getImageURL(Long productId) {
        String filename = "product" + productId + ".jpg";
        Path file = root.resolve(filename);
        String url = "";
        try {
            List<String> list2 = Files.walk(this.root, 1).filter(path -> !path.equals(this.root) && path.getFileName().toString().equals(filename)).map(this.root::relativize).map(path -> path.getFileName().toString()).collect(Collectors.toList());
            if (!list2.isEmpty()) {
                url = MvcUriComponentsBuilder
                        .fromMethodName(ImageController.class, "getFile", "product", file.getFileName().toString()).build().toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return url;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        productRepo.findAll().forEach(list::add);
        for (Product product:list) {
            String url = getImageURL(product.getProductId());
            product.setProductImage(url);
        }
        return list;
    }

    @Override
    public Product getProduct(Long productId) {
        String url = getImageURL(productId);
        Product product = productRepo.findById(productId).get();
        product.setProductImage(url);
        return product;
    }

    @Override
    public ResponseMessage checkProductApprovalStatus(Long productId) {
        Product product = productRepo.findById(productId).orElse(null);

        if (product == null) {
            return new ResponseMessage("Product with id " + productId + " has not been found");
        }

        return new ResponseMessage(product.getApprovalStatus());
    }

    @Override
    public List<Product> getProducts(ProductGetRequest productGetRequest) {

        ProductSearchRequest productSearchRequest = new ProductSearchRequest();
        productSearchRequest.setPhrase(productGetRequest.getPhrase());
        productSearchRequest.setCategory(productGetRequest.getCategory());

        List<Product> productList = searchProducts(productSearchRequest);

        switch (productGetRequest.getProductsGroup()) {
            case "all":
                productList = productList.stream().filter(product -> product.getApprovalStatus().equals("accepted")).collect(Collectors.toList());
                break;
            case "unconfirmed":
                productList = productList.stream().filter(product -> product.getApprovalStatus().equals("pending") || product.getApprovalStatus().equals("rejected")).collect(Collectors.toList());
                break;
            case "pending":
                productList = productList.stream().filter(product -> product.getApprovalStatus().equals("pending")).collect(Collectors.toList());
                break;
            case "rejected":
                productList = productList.stream().filter(product -> product.getApprovalStatus().equals("rejected")).collect(Collectors.toList());
                break;
            case "new":
                productList = productList.stream().filter(product -> product.getApprovalStatus().equals("accepted")).collect(Collectors.toList());
                LocalDate lastWeekBefore = LocalDate.now().minusDays(7);
                Instant lastWeekBeforeInstant = lastWeekBefore.atStartOfDay(ZoneId.systemDefault()).toInstant();
                LocalDate lastWeekBefore2 = LocalDate.ofInstant(lastWeekBeforeInstant, ZoneId.systemDefault());

                productList = productList.stream().filter(product -> {
                    Instant creationDate = Instant.parse(product.getCreationDate());
                    LocalDate creationDate2 = LocalDate.ofInstant(creationDate, ZoneId.systemDefault());
                    return creationDate2.isAfter(lastWeekBefore2) || creationDate2.isEqual(lastWeekBefore2);
                }).collect(Collectors.toList());
                break;
            default:
                productList = null;
        }

        return productList;
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
        }

        //change to logged in user id
        User productOwner = userService.findByUsername(userService.getLoggedUser().getUsername());
        if (productOwner == null) {
            return new ResponseMessage("Product " + productName + " owner has not been found");
        }
        product.setOwner(productOwner);
        String creationDate = new Date().toInstant().toString();
        product.setCreationDate(creationDate);

        productRepo.save(product);
        Product lastAddedProduct = getAllProducts().stream().filter(p -> p.getCreationDate().equals(creationDate)).findFirst().orElse(null);
        if (lastAddedProduct != null) {
            return new ResponseMessage(lastAddedProduct.getProductId() + " Product " + productName + " has been added successfully");
        } else {
            return new ResponseMessage("Product " + productName + " has not been found");
        }
    }

    @Override
    public ResponseMessage updateProduct(Long productId, ProductRequest productRequest) {

        String productName = "";
        Optional<Product> product = productRepo.findById(productId);
        if (product.isPresent()) {
            Product updatedProduct = product.get();

            User currentLoggedUser = userService.findByUsername(userService.getLoggedUser().getUsername());

            if (currentLoggedUser == null) {
                return new ResponseMessage("Current logged user has not been found");
            }

            if (!updatedProduct.getOwner().equals(currentLoggedUser)) {
                return new ResponseMessage("Only product owner is permitted to update product");
            }

            productName = productRequest.getProductName();

            ResponseMessage responseMessage = verify("update", "name", productName);
            if (responseMessage.getMessage().equals("Product name is valid")){
                updatedProduct.setProductName(productName);
            } else {
                return responseMessage;
            }

            Integer calories = productRequest.getCalories();

            ResponseMessage responseMessage2 = verify("update", "calories", calories);

            if (responseMessage2.getMessage().equals("Product calories are valid")) {
                updatedProduct.setCalories(calories);
            } else {
                return responseMessage2;
            }

            String categoryName = productRequest.getCategory();

            ResponseMessage responseMessage3 = verify("update", "category", categoryName);

            if (responseMessage3.getMessage().equals("Product category is valid")) {
                updatedProduct.setCategory(categoryRepo.findByCategoryName(categoryName));
            } else {
                return responseMessage3;
            }

            List<String> nutrients = productRequest.getNutrients();

            ResponseMessage responseMessage4 = verify("update", "list", nutrients);

            if (!(responseMessage4.getMessage().equals("Product nutrients are valid"))) {
                return responseMessage4;
            }

            for (String nutrientStatement: nutrients) {

                ResponseMessage responseMessage5 = verify("update", "nutrientStatement", nutrientStatement);

                if (!(responseMessage5.getMessage().equals("Product nutrient statement is valid"))) {
                    return responseMessage5;
                }
                String[] parts = nutrientStatement.split(";");
                String nutrientName = parts[0];
                Double nutrientAmount = Double.valueOf(parts[1]);

                ResponseMessage responseMessage6 = verify("update", "nutrientName", nutrientName);

                if (!(responseMessage6.getMessage().equals("Product nutrient name is valid"))) {
                    return responseMessage6;
                }

                ResponseMessage responseMessage7 = verify("update", "nutrientAmount", nutrientAmount);

                if (!(responseMessage7.getMessage().equals("Product nutrient amount is valid"))) {
                    return responseMessage7;
                }

                ProductNutrient productNutrient = updatedProduct.getNutrients().stream().filter(pn -> pn.getNutrient().getNutrientName().equals(nutrientName)).findFirst().orElse(null);

                if (productNutrient != null) {
                    productNutrient.setNutrientAmount(nutrientAmount);
                } else {
                    return new ResponseMessage( "Nutrient " + nutrientName + " belonging to product " + productName + " has not been found");
                }
            }

            updatedProduct.setApprovalStatus("pending");
            updatedProduct.setRejectExplanation(null);
            updatedProduct.setAssessmentDate(null);

            productRepo.save(updatedProduct);

            return new ResponseMessage("Product " + productName + " has been updated successfully");
        }
        return new ResponseMessage("Product " + productName + " has not been found");
    }

    @Override
    public ResponseMessage removeProduct(Long productId) {

        Optional<Product> product = productRepo.findById(productId);
        if (product.isPresent()) {
            Product removedProduct = product.get();

            User currentLoggedUser = userService.findByUsername(userService.getLoggedUser().getUsername());

            if (currentLoggedUser == null) {
                return new ResponseMessage("Current logged user has not been found");
            }

            if (!removedProduct.getOwner().equals(currentLoggedUser)) {
                return new ResponseMessage("Only product owner is permitted to remove product");
            }

            productRepo.delete(removedProduct);
            return new ResponseMessage("Product " + removedProduct.getProductName() + " has been removed successfully");
        }
            return new ResponseMessage("Product id " + productId + " has not been found");
    }

    @Override
    public List<Product> searchProducts(ProductSearchRequest productSearchRequest) {

        String category = productSearchRequest.getCategory();
        String phrase = productSearchRequest.getPhrase();

        List<Product> products = getAllProducts();

        if (!category.equals("")) {
            List<Category> categories = new ArrayList<>();
            categoryRepo.findAll().forEach(categories::add);
            if (categories.stream().map(Category::getCategoryName).filter(n -> n.equalsIgnoreCase(category)).findFirst().orElse(null) == null) {
                return new ArrayList<>();
            }
            products = products.stream().filter(product -> product.getCategory().getCategoryName().equalsIgnoreCase(category)).collect(Collectors.toList());
        }

        if (!phrase.equals("")) {
            products = products.stream().filter(product -> product.getProductName().toLowerCase().contains(phrase.toLowerCase())).collect(Collectors.toList());
        }
        return products;
    }

    @Override
    public ResponseMessage assessProduct(ItemAssessRequest itemAssessRequest) {
        Product assessedProduct = productRepo.findById(itemAssessRequest.getItemId()).orElse(null);

        if (assessedProduct != null) {

            if (!assessedProduct.getApprovalStatus().equals("pending")) {
                return new ResponseMessage("Product has been assessed yet");
            }

            String assessment = itemAssessRequest.getAssessment();
            String rejectExplanation = itemAssessRequest.getRejectExplanation();
            String assessmentDate = new Date().toInstant().toString();
            assessedProduct.setAssessmentDate(assessmentDate);

            switch (assessment) {
                case "accept":
                    assessedProduct.setApprovalStatus("accepted");
                    if (rejectExplanation != null) {
                        return new ResponseMessage("Rejection explanation is only required for product rejection");
                    }
                    productRepo.save(assessedProduct);
                    return new ResponseMessage("Product " + assessedProduct.getProductName() + " has been accepted");
                case "reject":
                    assessedProduct.setApprovalStatus("rejected");
                    if (rejectExplanation == null || rejectExplanation.equals("")) {
                        return new ResponseMessage("Rejection explanation is required for product rejection");
                    }
                    assessedProduct.setRejectExplanation(rejectExplanation);
                    productRepo.save(assessedProduct);
                    return new ResponseMessage("Product " + assessedProduct.getProductName() + " has been rejected");
                default:
                    return new ResponseMessage("Wrong assessment");
            }
        }
            return new ResponseMessage("Proper product has not been found");

    }

}