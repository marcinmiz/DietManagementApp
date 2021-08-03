package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.entities.security.UserRole;
import agh.edu.pl.diet.payloads.request.ProductRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.RoleRepo;
import agh.edu.pl.diet.repos.UserRepo;
import agh.edu.pl.diet.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    private ResponseMessage verify(String mode, String type, Object item) {
        switch (type) {
            case "username":
                String name = String.valueOf(item);
                if (name == null) {
                    return new ResponseMessage("Username name has to be given");
                } else if (name.length() < 2 || name.length() > 40) {
                    return new ResponseMessage("Username name has to have min 2 and max 40 characters");
                } else if (!(name.matches("^[a-zA-Z ]+$"))) {
                    return new ResponseMessage("Username name has to contain only letters and spaces");
                } else if (!mode.equals("username") && userRepo.findByName(name) != null) {
                    return new ResponseMessage("Username with this name exists yet");
                } else {
                    return new ResponseMessage("Username name is valid");
                }
            case "password":
                if (item == null) {
                    return new ResponseMessage("Password calories has to be given");
                }

                String password = String.valueOf(item);

                if (password.toString().length() < 2) {
                    return new ResponseMessage("Password has to have min 3 characters");
                } else if (!(password.toString().matches("^0$") || password.toString().matches("^(-)?[1-9]\\d*$"))) {
                    return new ResponseMessage("Password has to contain only digits");
                } else if (password.length() < 0) {
                    return new ResponseMessage("Password has to be greater or equal 0");
                } else {
                    return new ResponseMessage("Password are valid");
                }
//            case "category":
//                String categoryName = String.valueOf(item);
//                if (categoryName == null) {
//                    return new ResponseMessage("Product category has to be given");
//                } else if (categoryName.equals("")) {
//                    return new ResponseMessage("Product category has to be chosen");
//                } else if (categoryRepo.findByCategoryName(categoryName) == null) {
//                    return new ResponseMessage("Product category does not exist");
//                } else {
//                    return new ResponseMessage("Product category is valid");
//                }
//            case "list":
//                List<String> nutrients = (List<String>) item;
//                if (nutrients == null) {
//                    return new ResponseMessage("Product nutrients has to be given");
//                } else if (nutrients.isEmpty()) {
//                    return new ResponseMessage("At least 1 product nutrient is required");
//                } else {
//                    return new ResponseMessage("Product nutrients are valid");
//                }



        }

        return new ResponseMessage("Invalid type");
    }

    //@Override
    public User createUser(User user, Set<UserRole> userRoles) {
        User localUser = userRepo.findByUsername(user.getUsername());

        if (localUser != null) {
            LOG.info("user {} already exists. Nothing will be done.", user.getUsername());
        } else {
            for (UserRole ur : userRoles) {
                roleRepo.save(ur.getRole());
            }

            user.getUserRoles().addAll(userRoles);

            localUser = userRepo.save(user);
        }

        return localUser;
    }

    @Override
    public User getCreateUser(User user, Set<UserRole> userRoles) throws Exception {
        return null;
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public ResponseMessage getCreateUser(ProductRequest userRequest) {
        return null;
    }

    @Override
    public User save() {
        return null;
    }

//    @Override
//    public List<Product> getAllProducts() {
//        return null;
//    }
//
//    @Override
//    public Product getProduct(Long product_id) {
//        return null;
//    }
//
//    @Override
//    public ResponseMessage addNewProduct(ProductRequest productRequest) {
//        return null;
//    }
//
//    @Override
//    public ResponseMessage updateProduct(Long productId, ProductRequest productRequest) {
//        return null;
//    }
//
//    @Override
//    public ResponseMessage removeProduct(Long productId) {
//        return null;
//    }

}