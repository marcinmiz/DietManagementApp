package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.entities.Product;
import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.repos.ProductRepo;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController
{
	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private UserService userService;

	@GetMapping("/products")
	public String notes(Principal principal, Model model)
	{
		User user = (User) userService.loadUserByUsername(principal.getName());

		List<Product> products = productRepo.findByUserId(user.getId());
		model.addAttribute("notes", products);
		model.addAttribute("user", user);

		return "notes";
	}

	@PostMapping("/addproduct")
	public String addNote(Principal principal, String title, String note, String calories, String totalFat, String totalCarbohydrate, String protein, String sodium)
	{
		User user = (User) userService.loadUserByUsername(principal.getName());

		Product newProduct = new Product();
		newProduct.setTitle(title);
		newProduct.setNote(note);
		newProduct.setCalories(calories);
		newProduct.setTotalFat(totalFat);
		newProduct.setTotalCarbohydrate(totalCarbohydrate);
		newProduct.setProtein(protein);
		newProduct.setSodium(sodium);
		newProduct.setUserId(user.getId());

		productRepo.save(newProduct);

		return "redirect:/products";
	}
}
