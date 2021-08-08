package agh.edu.pl.diet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DietApplication implements CommandLineRunner {


//    @Autowired
//    private UserService userService;
//    @Autowired
//    private ProductService productService;
//    @Autowired
//    private ImageService imageService;

    public static void main(String[] args) {
        SpringApplication.run(DietApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

//    @Override
//    public void run(String... args) throws Exception {
//        imageService.init();
//        User user1 = new User();
//        user1.setUsername("admin");
//        user1.setPassword(SecurityUtility.passwordEncoder().encode("admin"));
//        user1.setEmail("admin@gmail.com");
//        Set<UserRole> userRoles = new HashSet<>();
//        Role role1 = new Role();
//        role1.setRoleId(0);
//        role1.setName("ROLE_ADMIN");
//        userRoles.add(new UserRole(user1, role1));
//
//        userService.createUser(user1, userRoles);
//
//        Recipe recipe1 = new Recipe();
//        recipe1.setRecipeName("cheesecake");
//        recipe1.setOwner(user1);
//
//        Product product1 = new Product();
//        product1.setTitle("white cheese");
//        product1.setCalories("34");
//        product1.setCategory("Dairy");
//        product1.setProtein("26");
//        product1.setTotalCarbohydrate("12");
//
//        productService.save(product1);
//
//        Product product2 = new Product();
//        product2.setTitle("egg");
//        product2.setCalories("12");
//        product2.setCategory("Dairy");
//        product2.setProtein("43");
//        product2.setTotalCarbohydrate("32");
//
//        productService.save(product2);
////
////        RecipeProduct recipeProduct1 = new RecipeProduct();
////        recipeProduct1.setRecipeProductId(recipe1, product1);
////        recipeProduct1.setQuantity(1000.0);
////        recipeProduct1.setUnit("g");
////
////        recipe1.getRecipeProducts().add(recipeProduct1);
////
//         recipeRepo.save(recipe1);
////
//    }
}