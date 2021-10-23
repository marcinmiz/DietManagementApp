package agh.edu.pl.diet;

import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.DailyMenuRepo;
import agh.edu.pl.diet.repos.RecipeRepo;
import agh.edu.pl.diet.services.DailyMenuService;
import agh.edu.pl.diet.services.ImageService;
import agh.edu.pl.diet.services.MealService;
import agh.edu.pl.diet.services.RecipeService;
import agh.edu.pl.diet.services.impl.MealServiceImpl;
import agh.edu.pl.diet.services.impl.RecipeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan({"agh.edu.pl"})
public class DietApplication implements CommandLineRunner {
//    @Autowired
//    DailyMenuRepo dailyMenuRepo;
//    @Autowired
//    private RecipeRepo recipeRepo;
//    @Autowired
//    private MealService mealService;
//    @Autowired
//    DailyMenuService dailyMenuService;

    public static void main(String[] args) {
        SpringApplication.run(DietApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

//       ResponseMessage message = mealService.addNewMeal("breakfast", recipeRepo.findByRecipeId(957L), dailyMenuRepo.findById(1L).get());
//        System.out.println("message: " + message.getMessage());
//
//       ResponseMessage message2 = dailyMenuService.addNewDailyMenu("breakfast", recipeRepo.findByRecipeId(957L), dailyMenuRepo.findById(1L).get());
//        System.out.println("message2: " + message2.getMessage());
    }

}