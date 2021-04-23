package agh.edu.pl.diet.controllers;

import agh.edu.pl.diet.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class ResourceController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping(value="/product/removeList", method= RequestMethod.POST)
	public String removeList(
            @RequestBody ArrayList<String> productIdList, Model model
			){
		
		for (String id : productIdList) {
			String productId =id.substring(8);
			productService.removeOne(Long.parseLong(productId));
		}
		
		return "delete success";
	}
}
